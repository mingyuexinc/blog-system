package com.kj.blog.handler;

import com.kj.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 对加了Controller方法的异常进行处理
@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody  // 返回json数据
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
