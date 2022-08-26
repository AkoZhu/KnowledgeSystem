package com.Ako.wiki.controller;

//import com.Ako.wiki.domain.Doc;

import com.Ako.wiki.req.DocQueryReq;
import com.Ako.wiki.req.DocSaveReq;
import com.Ako.wiki.resp.DocQueryResp;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.service.DocService;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;


@RestController // Used when it returns a String.
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all") // we can change "/doc/list" to "/list" since we add @RequestMapping("/doc");
    public CommonResp all() {
        // Don't let the whole information, the Doc class show in Controller.
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list") // we can change "/doc/list" to "/list" since we add @RequestMapping("/doc");
    public CommonResp list(@Valid DocQueryReq req) {
        // Don't let the whole information, the Doc class show in Controller.
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        // Don't let the whole information, the Doc class show in Controller.
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        // Don't let the whole information, the Doc class show in Controller.
        CommonResp resp = new CommonResp<>();
        if(!ObjectUtils.isEmpty(idsStr)){
            List<String> list = Arrays.asList(idsStr.split(","));
            docService.delete(list);
        }
        return resp;
    }

    @GetMapping("/file-content/{id}") // we can change "/doc/list" to "/list" since we add @RequestMapping("/doc");
    public CommonResp fileContent(@PathVariable Long id) {
        // Don't let the whole information, the Doc class show in Controller.
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.fileContent(id);
        resp.setContent(content);
        return resp;
    }

}
