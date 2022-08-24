package com.Ako.wiki.controller;

//import com.Ako.wiki.domain.Ebook;
import com.Ako.wiki.req.EbookQueryReq;
import com.Ako.wiki.req.EbookSaveReq;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.EbookQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.service.EbookService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController // Used when it returns a String.
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list") // we can change "/ebook/list" to "/list" since we add @RequestMapping("/ebook");
    public CommonResp list(@Valid EbookQueryReq req) {
        // Don't let the whole information, the Ebook class show in Controller.
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {
        // Don't let the whole information, the Ebook class show in Controller.
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        // Don't let the whole information, the Ebook class show in Controller.
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
