package com.kj.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.kj.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.bytecode.stackmap.TypeData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Slf4j
@Aspect
@Component
public class CacheAspect {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 切点,即注解所加的位置
    @Pointcut("@annotation(com.kj.blog.common.cache.CacheAnnotation)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        try {
            Signature signature = joinPoint.getSignature();
            // 类名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            // 调用的方法名
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[joinPoint.getArgs().length];

            Object[] args = joinPoint.getArgs();
            String params = "";
            for (int i = 0; i < args.length;i++) {
                if (args[i]!=null){
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                }else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)){
                params = DigestUtils.md5Hex(params);
            }

            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            // 获取Cache注解
            CacheAnnotation annotation = method.getAnnotation(CacheAnnotation.class);
            // 缓存过期时间
            long expire = annotation.expire();
            // 缓存名称
            String name = annotation.name();

            String redisKey = name+"::"+ className + "::"+methodName+
                    "::"+params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)){
                log.info("走了缓存,{},{}",className,methodName);
                return JSON.parseObject(redisValue,Result.class);
            }
            // 没走缓存,调用方法
            Object proceed = null;
            proceed = joinPoint.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存,{},{}",className,methodName);
            return proceed;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }
}
