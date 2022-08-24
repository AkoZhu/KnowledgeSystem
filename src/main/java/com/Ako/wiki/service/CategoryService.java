package com.Ako.wiki.service;

import com.Ako.wiki.domain.Category;
import com.Ako.wiki.domain.CategoryExample;
import com.Ako.wiki.mapper.CategoryMapper;
import com.Ako.wiki.req.CategoryQueryReq;
import com.Ako.wiki.req.CategorySaveReq;
import com.Ako.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        // Only in active for the first Select.
        // It is better to write PageHelper tightly with select method.
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        // If we use the second Select, then it fails to limit the page.
        // categoryList = categoryMapper.selectByExample(categoryExample);

        // It gives total Row nums and page number. 
        PageInfo<Category> pageInfo = new PageInfo(categoryList);
        LOG.info("Total Lines Num: {}", pageInfo.getTotal());
        LOG.info("Total Page Num: {}", pageInfo.getPages());
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
//            //  CategoryResp categoryResp = new CategoryResp();
//            // categoryResp.setId(category.getId()); // You can use it, but it is redundant.
//            // It is better to use API provided by Spring.
//            // You do not manually set id, name, and a bunch of things.
//            //  BeanUtils.copyProperties(category, categoryResp);
//            -- Copy the Object
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }
        
        // -- Copy all list.
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        
        return pageResp;
    }
    /**
     * 
     * Save
     *  
    */
    public void save(CategorySaveReq req){
        // Save have two types: 1. Modify, 2. Add.
        Category category = CopyUtil.copy(req, Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // Add
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            // Update
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * Delete 
    */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
