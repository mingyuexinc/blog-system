package com.kj.blog.controller;

import com.kj.blog.service.LoginService;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        // 登录 验证用户 访问数据表
        return loginService.login(loginParams);
    }
}
