package com.Ako.wiki.controller;

//import com.Ako.wiki.domain.User;
import com.Ako.wiki.req.UserQueryReq;
import com.Ako.wiki.req.UserSaveReq;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.UserQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.service.UserService;

import org.springframework.util.DigestUtils;
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
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list") // we can change "/user/list" to "/list" since we add @RequestMapping("/user");
    public CommonResp list(@Valid UserQueryReq req) {
        // Don't let the whole information, the User class show in Controller.
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        // Don't let the whole information, the User class show in Controller.
        CommonResp resp = new CommonResp<>();
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        // Don't let the whole information, the User class show in Controller.
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
