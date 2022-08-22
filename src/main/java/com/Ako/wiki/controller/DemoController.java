package com.Ako.wiki.controller;

import com.Ako.wiki.domain.Demo;
import com.Ako.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController // Used when it returns a String.
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/list") // we can change "/demo/list" to "/list" since we add @RequestMapping("/demo");
    public List<Demo> list() {
        return demoService.list();
    }
}
