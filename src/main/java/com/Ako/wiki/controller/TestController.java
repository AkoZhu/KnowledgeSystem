package com.Ako.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Used when it returns a String.
// @Controller // Used when it returns a web page
public class TestController {

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
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
}
