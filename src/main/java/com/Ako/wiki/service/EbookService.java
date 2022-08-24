package com.Ako.wiki.service;

import com.Ako.wiki.domain.Ebook;
import com.Ako.wiki.domain.EbookExample;
import com.Ako.wiki.mapper.EbookMapper;
import com.Ako.wiki.req.EbookReq;
import com.Ako.wiki.resp.EbookResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.util.CopyUtil;
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

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
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
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp<>();

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        
        return pageResp;
    }
}
