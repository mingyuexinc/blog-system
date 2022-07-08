package com.kj.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 定义返回结果参数
public class Result {
    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    public static Result fail(int code,String msg){
        return new Result(false,code,"fail",null);
    }
}
