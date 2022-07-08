package com.kj.blog.handler;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.service.LoginService;
import com.kj.blog.utils.UserThreadLocal;
import com.kj.blog.vo.ErrorCode;
import com.kj.blog.vo.Result;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断请求的接口路径是否为HandlerMethod(访问的Controller方法)
        // 判断token是否为空，如果为空则未登录，应拦截
        // 如果token不为空，验证token是否正确
        // 如果认证成功，放行即可
        if (!(handler instanceof HandlerMethod)){
            return true; // 放行
        }
        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            // 转为json格式
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            // 转为json格式
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 验证成功,放行
        // 存放用户信息
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 如果不删除会有内存泄露的风险
        UserThreadLocal.remove();
    }
}
