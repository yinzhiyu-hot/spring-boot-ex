package com.example.common.annotations;

import java.lang.annotation.*;

/**
 * @Description 自定义注解，反序列化会根据此注解进行解析
 * @PackagePath com.example.common.annotations.XmlElementAnno
 * @Author YINZHIYU
 * @Date 2020/5/8 13:45
 * @Version 1.0.0.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlElementAnno {

}