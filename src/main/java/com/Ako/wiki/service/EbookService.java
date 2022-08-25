package com.Ako.wiki.service;

import com.Ako.wiki.domain.Ebook;
import com.Ako.wiki.domain.EbookExample;
import com.Ako.wiki.mapper.EbookMapper;
import com.Ako.wiki.req.EbookQueryReq;
import com.Ako.wiki.req.EbookSaveReq;
import com.Ako.wiki.resp.EbookQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.util.CopyUtil;
import com.Ako.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        // Only in active for the first Select.
        // It is better to write PageHelper tightly with select method.
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        // If we use the second Select, then it fails to limit the page.
        // ebookList = ebookMapper.selectByExample(ebookExample);

        // It gives total Row nums and page number. 
        PageInfo<Ebook> pageInfo = new PageInfo(ebookList);
        LOG.info("Total Lines Num: {}", pageInfo.getTotal());
        LOG.info("Total Page Num: {}", pageInfo.getPages());
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            //  EbookResp ebookResp = new EbookResp();
//            // ebookResp.setId(ebook.getId()); // You can use it, but it is redundant.
//            // It is better to use API provided by Spring.
//            // You do not manually set id, name, and a bunch of things.
//            //  BeanUtils.copyProperties(ebook, ebookResp);
//            -- Copy the Object
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }
        
        // -- Copy all list.
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        
        return pageResp;
    }
    /**
     * 
     * Save
     *  
    */
    public void save(EbookSaveReq req){
        // Save have two types: 1. Modify, 2. Add.
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // Add
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else{
            // Update
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * Delete 
    */
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }
}
