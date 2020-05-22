package com.example.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * @Description 编号工具
 * @PackagePath com.example.common.utils.NumberUtils
 * @Author YINZHIYU
 * @Date 2020/5/22 11:32
 * @Version 1.0.0.0
 **/
public class NumberUtils {

    /*
     * @Description 时间戳
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/20 10:54
     * @Auther YINZHIYU
     */
    public static String genralNumber() {
        ThreadUtil.sleep(10);
        return String.valueOf(DateUtil.date().getTime());
    }

    /*
     * @Description 前缀+时间戳+5位随机数
     * @Params ==>
     * @Param prefix
     * @Return java.lang.String
     * @Date 2020/5/20 10:55
     * @Auther YINZHIYU
     */
    public static String genralNumber(String prefix) {
        ThreadUtil.sleep(10);
        StringBuilder number = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            number.append(prefix).append("-");
        }
        number.append(DateUtil.date().getTime());
        number.append(RandomUtil.randomNumbers(5));
        return number.toString();
    }

    /*
     * @Description 前缀+时间格式化规则+5位随机数
     * @Params ==>
     * @Param prefix
     * @Param format
     * @Return java.lang.String
     * @Date 2020/5/20 10:55
     * @Auther YINZHIYU
     */
    public static String genralNumber(String prefix, String format) {
        ThreadUtil.sleep(10);
        StringBuilder number = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            number.append(prefix).append("-");
        }
        number.append(DateUtil.format(DateUtil.date(), format));
        number.append(RandomUtil.randomNumbers(5));
        return number.toString();
    }
}
