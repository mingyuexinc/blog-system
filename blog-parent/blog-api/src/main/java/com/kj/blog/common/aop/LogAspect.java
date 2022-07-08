package com.kj.blog.common.aop;

import com.alibaba.fastjson.JSON;
import com.kj.blog.utils.HttpContextUtils;
import com.kj.blog.utils.IpUtils;
import com.sun.net.httpserver.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect  // 切面,定义了通知和切点的关系
@Slf4j
public class LogAspect {

    // 切点,即注解所加的位置
    @Pointcut("@annotation(com.kj.blog.common.aop.LogAnnotation)")
    public void pointcut(){}

    // 环绕通知
    @Around("pointcut()")
    public Object Log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = joinPoint.proceed();
        // 执行时长
        long time = System.currentTimeMillis()-beginTime;
        // 保存日志
        recordLog(joinPoint,time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        log.info("===================log start=============================");
        log.info("module:{}",annotation.module());
        log.info("operation:{}",annotation.operator());

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className + "." + methodName + "()");

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        // 获取request 设置ip地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));


        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");
    }
}
