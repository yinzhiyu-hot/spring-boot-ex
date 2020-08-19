package com.example.common.annotations;

import java.lang.annotation.*;

/**
 * @Description 分片数注解
 * @PackagePath com.example.common.annotations.ShardingTotalCount
 * @Author YINZHIYU
 * @Date 2020/6/16 18:21
 * @Version 1.0.0.0
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingTotalCount {
    int value();
}
