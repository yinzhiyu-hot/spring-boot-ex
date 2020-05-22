package com.example.common.utils;

/**
 * @Description 转换工具
 * @PackagePath com.example.common.utils.CastUtil
 * @Author YINZHIYU
 * @Date 2020/5/22 11:29
 * @Version 1.0.0.0
 **/
public class CastUtil {

    /*
     * @Description 转换
     * @Params ==>
     * @Param object
     * @Return T
     * @Date 2020/5/18 9:08
     * @Auther YINZHIYU
     */
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
