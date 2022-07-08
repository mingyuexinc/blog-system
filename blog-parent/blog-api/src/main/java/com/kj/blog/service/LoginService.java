package com.kj.blog.service;

import com.kj.blog.pojo.SysUser;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

@Transactional  // 事务注解(建议加在接口上)
public interface LoginService {
    /**
     * 登录功能
     * @param loginParams 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/5 12:58
     */
    
    Result login(LoginParams loginParams);


    /**
     * Token校验
     * @param token 
     * @return com.kj.blog.pojo.SysUser 
     * @create 2022/6/7 10:59
     */
    
    SysUser checkToken(String token);

    
    /**
     * 退出登录
     * @param token 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/7 15:23
     */
    
    Result logout(String token);


    /**
     * 注册
     * @param loginParams 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/7 17:22
     */
    
    Result register(LoginParams loginParams);
}
