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
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParams loginParams){
        return loginService.register(loginParams);
    }
}
