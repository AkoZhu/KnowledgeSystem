package com.Ako.wiki.service;

import com.Ako.wiki.domain.User;
import com.Ako.wiki.domain.UserExample;
import com.Ako.wiki.exception.BusinessException;
import com.Ako.wiki.exception.BusinessExceptionCode;
import com.Ako.wiki.mapper.UserMapper;
import com.Ako.wiki.req.UserQueryReq;
import com.Ako.wiki.req.UserSaveReq;
import com.Ako.wiki.resp.UserQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.util.CopyUtil;
import com.Ako.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq req){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        // Only in active for the first Select.
        // It is better to write PageHelper tightly with select method.
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        // If we use the second Select, then it fails to limit the page.
        // userList = userMapper.selectByExample(userExample);

        // It gives total Row nums and page number. 
        PageInfo<User> pageInfo = new PageInfo(userList);
        LOG.info("Total Lines Num: {}", pageInfo.getTotal());
        LOG.info("Total Page Num: {}", pageInfo.getPages());
//        List<UserResp> respList = new ArrayList<>();
//        for (User user : userList) {
//            //  UserResp userResp = new UserResp();
//            // userResp.setId(user.getId()); // You can use it, but it is redundant.
//            // It is better to use API provided by Spring.
//            // You do not manually set id, name, and a bunch of things.
//            //  BeanUtils.copyProperties(user, userResp);
//            -- Copy the Object
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }
        
        // -- Copy all list.
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        
        return pageResp;
    }
    /**
     * 
     * Save
     *  
    */
    public void save(UserSaveReq req){
        // Save have two types: 1. Modify, 2. Add.
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            if(selectByLoginName(req.getLoginName()) == null){
                // Add
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                // User exists.
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            // Update
            userMapper.updateByPrimaryKey(user);
        }
    }

    /**
     * Delete 
    */
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (ObjectUtils.isEmpty(loginName)) return null;
        
        criteria.andLoginNameEqualTo(loginName);
        List<User> userlist = userMapper.selectByExample(userExample);
        if(userlist.isEmpty()) return null;
        else return userlist.get(0);
    }
}
