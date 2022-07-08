package com.kj.blog.service;

import com.kj.blog.pojo.Comment;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @return com.kj.blog.vo.Result
     * @create 2022/6/7 10:15
     */

    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return com.kj.blog.pojo.SysUser 
     * @create 2022/6/10 10:56
     */
    
    SysUser findUserByAccount(String account);

    /**
     * 保存用户信息
     * @param sysUser 
     * @return void 
     * @create 2022/6/10 15:42
     */
    
    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
