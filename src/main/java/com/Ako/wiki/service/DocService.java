package com.Ako.wiki.service;

import com.Ako.wiki.domain.Content;
import com.Ako.wiki.domain.Doc;
import com.Ako.wiki.domain.DocExample;
import com.Ako.wiki.mapper.ContentMapper;
import com.Ako.wiki.mapper.DocMapper;
import com.Ako.wiki.req.DocQueryReq;
import com.Ako.wiki.req.DocSaveReq;
import com.Ako.wiki.resp.DocQueryResp;
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

import java.util.ArrayList;
import java.util.List;


@Service
public class DocService {
    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        // While condition 
        // DocExample.Criteria criteria = docExample.createCriteria();
        List<Doc> docList = docMapper.selectByExample(docExample);
        
        // List copy
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }


    /**
     * 
     *  Query a list of books
     * 
    */
    public PageResp<DocQueryResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // Only in active for the first Select.
        // It is better to write PageHelper tightly with select method.
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        // If we use the second Select, then it fails to limit the page.
        // docList = docMapper.selectByExample(docExample);

        // It gives total Row nums and page number. 
        PageInfo<Doc> pageInfo = new PageInfo(docList);
        LOG.info("Total Lines Num: {}", pageInfo.getTotal());
        LOG.info("Total Page Num: {}", pageInfo.getPages());
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
//            //  DocResp docResp = new DocResp();
//            // docResp.setId(doc.getId()); // You can use it, but it is redundant.
//            // It is better to use API provided by Spring.
//            // You do not manually set id, name, and a bunch of things.
//            //  BeanUtils.copyProperties(doc, docResp);
//            -- Copy the Object
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }
        
        // -- Copy all list.
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp<>();

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        
        return pageResp;
    }
    /**
     * 
     * Save
     *  
    */
    public void save(DocSaveReq req){
        // Save have two types: 1. Modify, 2. Add.
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // Add
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            // Update
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count == 0){
                contentMapper.insert(content);
            }
        }
    }

    /**
     * Delete 
    */
    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        List<Long> idsList = new ArrayList<>();
        for(String str:ids){
            idsList.add(Long.parseLong(str));
        }
        criteria.andIdIn(idsList);
        docMapper.deleteByExample(docExample);
    }



    public String fileContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        if(content == null) return null;
        return content.getContent();
    }
}
