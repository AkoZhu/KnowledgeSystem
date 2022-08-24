package com.Ako.wiki.controller;

//import com.Ako.wiki.domain.Category;

import com.Ako.wiki.req.CategoryQueryReq;
import com.Ako.wiki.req.CategorySaveReq;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.CategoryQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController // Used when it returns a String.
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list") // we can change "/category/list" to "/list" since we add @RequestMapping("/category");
    public CommonResp list(@Valid CategoryQueryReq req) {
        // Don't let the whole information, the Category class show in Controller.
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        // Don't let the whole information, the Category class show in Controller.
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        // Don't let the whole information, the Category class show in Controller.
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
