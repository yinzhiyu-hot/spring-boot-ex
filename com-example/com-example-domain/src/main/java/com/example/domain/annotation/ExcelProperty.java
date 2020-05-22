package com.example.domain.annotation;


import java.lang.annotation.*;

/**
 * @Description Excel注解
 * @PackagePath com.example.domain.annotation.ExcelProperty
 * @Author YINZHIYU
 * @Date 2020/5/8 13:54
 * @Version 1.0.0.0
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    /**
     * 表头名字
     */
    String excelTitle();

}
