package com.example.common.constants;

/**
 * @Description 常量信息
 * @PackagePath com.example.common.constants.BaseConstants
 * @Author YINZHIYU
 * @Date 2020/5/8 13:46
 * @Version 1.0.0.0
 **/
public interface BaseConstants {
 
    /**
     * 用户信息头
     */
    String USERINFO_HEADER = "x-userinfo-header";

    /**
     * springboot环境的端口key
     */
    String LOCAL_SERVER_PORT = "local.server.port";

    /**
     * 拆分字符
     */
    char SPLIT_CHAR_DUN_HAO = ',';
    char SPLIT_CHAR_JIN_HAO = '#';
    char SPLIT_CHAR_SHU_XIAN = '|';
    CharSequence SPLIT_CHAR_SEQUENCE_DUN_HAO = ",";
    CharSequence SPLIT_CHAR_SEQUENCE_JIN_HAO = "#";
    CharSequence SPLIT_CHAR_SEQUENCE_SHU_XIAN = "|";
}
