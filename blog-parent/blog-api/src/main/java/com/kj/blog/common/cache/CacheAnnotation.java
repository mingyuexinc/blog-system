package com.kj.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAnnotation {
    // 过期时间
    long expire() default 1*60*1000;
    // 缓存标识
    String name() default "";
}
