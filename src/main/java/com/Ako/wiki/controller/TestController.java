package com.Ako.wiki.controller;

import com.Ako.wiki.domain.Test;
import com.Ako.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController // Used when it returns a String.
// @Controller // Used when it returns a web page
public class TestController {


    // Get value from config file.
    @Value("${test.hello: TEST}") // Add the default value TEST
                                  // if we can't find test.hello in config file.
    private String testHello;

    @Resource
    private TestService testService;

    /**
     * The usual http request methods are following four:
     * GET, POST, PUT, DELETE
     *
     * GET: when we use brower to the website, it is a GET request.
     *
     * /user? id = 1
     * /user/1 -> Restful style
     *
     * Since we use @RequestMapping, the method can be used by all four requests. If
     * you just want it to be used by GET, use @GetMapping/ @PostMapping ... ;
     * Or you can set the value: @RequestMapping(value = "hello", method = RequestMethod.Get);
     *
     * If it can only be obtained by user 1, then set it as @RequestMapping(value = "/user/1", method = RequestMethod.Get)
     * Just replace "/hello" with "/user/1"
     * @return
     */
    // @PostMapping("/hello")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!" + testHello;
    }

    @RequestMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World! Post, " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {return testService.list();}
}
