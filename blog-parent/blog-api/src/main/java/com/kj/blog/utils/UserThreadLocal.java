package com.kj.blog.utils;

import com.kj.blog.pojo.SysUser;

public class UserThreadLocal {

    private UserThreadLocal(){}
    // 单例模式
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
