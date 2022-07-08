package com.kj.blog.controller;

import com.kj.blog.pojo.SysUser;
import com.kj.blog.utils.UserThreadLocal;
import com.kj.blog.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
