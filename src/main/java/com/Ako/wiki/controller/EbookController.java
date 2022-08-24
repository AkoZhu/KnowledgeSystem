package com.Ako.wiki.controller;

//import com.Ako.wiki.domain.Ebook;
import com.Ako.wiki.req.EbookReq;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.EbookResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController // Used when it returns a String.
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list") // we can change "/ebook/list" to "/list" since we add @RequestMapping("/ebook");
    public CommonResp list(EbookReq req) {
        // Don't let the whole information, the Ebook class show in Controller.
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
