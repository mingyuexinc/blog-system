package com.kj.blog.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.utils.JWTUtils;
import com.kj.blog.vo.ErrorCode;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String salt = "mszlu!@#";  // 加密盐值

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;  // springboot整合redis

    /*1.检查参数是否合法(不能为空)
    * 2.根据用户名和密码去数据库中查询用户否存在
    * 3.如果不存在则登录失败
    * 4.如果存在则用jwt生成token返回前端 (密码还要先加密)
    * 5.token放入redis中,并设置过期时间等参数
    * */
    @Override
    public Result login(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        // blank和empty的区别：前者连续空格也算空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt); // 加密
        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null; // token为空
        }
        // 校验
        Map<String, Object> objectMap = JWTUtils.checkToken(token);
        if (objectMap == null){  // 解析失败(token错误)
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("Token_" + token);
        if (StringUtils.isBlank(userJson)){  // redis中不存在
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);  // 类型转换

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParams loginParams) {
        /*1. 判断参数是否合法
        * 2. 判断账户是否存在,存在则返回账户已存在
        * 3. 不存在,注册用户
        * 4. 生成token
        * 5. 存入redis并返回
        * 6. 注意加上事务 如果中间的任何过程出现问题,注册用户需要回滚
        * */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();
        if (StringUtils.isBlank(account) ||
            StringUtils.isBlank(password)||
            StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        // 注册用户并填写相关信息
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        // 生成Token,存入redis中并返回
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
