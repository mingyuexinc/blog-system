package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.kj.blog.mapper.SysUserMapper;
import com.kj.blog.pojo.Comment;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.vo.ErrorCode;
import com.kj.blog.vo.LoginUserVo;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        // 避免用户名为空
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("海岚");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        // 密码在数据库中已经加密过了
        queryWrapper.eq(SysUser::getPassword,password);
        // 还需查询其他数据
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1"); // 确保只查出一条

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token){
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1"); // 确保只查出一条
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        // 这里保存的用户id是分布式id
        sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("明月心");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(userVo.getId()));
        return userVo;
    }
}
