package com.example.common.annotations;

import java.lang.annotation.*;

/**
 * @Description 时间表达式注解
 * @PackagePath com.example.common.annotations.CronExpression
 * @Author YINZHIYU
 * @Date 2020/6/16 18:22
 * @Version 1.0.0.0
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CronExpression {
    String value();
}
