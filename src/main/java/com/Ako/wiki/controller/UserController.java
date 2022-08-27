package com.Ako.wiki.controller;

import com.Ako.wiki.req.UserLoginReq;
//import com.Ako.wiki.domain.User;
import com.Ako.wiki.req.UserQueryReq;
import com.Ako.wiki.req.UserResetPassword;
import com.Ako.wiki.req.UserSaveReq;
import com.Ako.wiki.resp.CommonResp;
import com.Ako.wiki.resp.UserQueryResp;
import com.Ako.wiki.resp.PageResp;
import com.Ako.wiki.resp.UserLoginResp;
import com.Ako.wiki.service.UserService;
import com.Ako.wiki.util.SnowFlake;
import com.alibaba.fastjson.JSONObject;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController // Used when it returns a String.
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;


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

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPassword req) {
        // Don't let the whole information, the User class show in Controller.
        CommonResp resp = new CommonResp<>();
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        // Don't let the whole information, the User class show in Controller.
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        // Don't let the whole information, the User class show in Controller.
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        
        Long token = snowFlake.nextId();
        LOG.info("Generate Single sign-on token: {}, and save it into redis.", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }
}
