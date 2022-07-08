package com.kj.blog.common.aop;

import java.lang.annotation.*;

// method:表示注解放在方法上面
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default ""; // 参数1:模块名称

    String operator() default ""; // 参数2:操作类型
}
