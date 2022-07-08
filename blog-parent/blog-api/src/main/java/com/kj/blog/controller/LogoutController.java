package com.kj.blog.controller;

import com.kj.blog.service.LoginService;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result Logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
