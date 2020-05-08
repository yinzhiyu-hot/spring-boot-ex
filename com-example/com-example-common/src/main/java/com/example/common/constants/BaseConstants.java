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
     * 日记保存条数
     */
    Integer LOG_TOTAL_NUM = 50;


    String DEFAULT_ZONE = "+08:00";

    /**
     * 用户信息头
     */
    String USERINFO_HEADER = "x-userinfo-header";

    /**
     * 系统的redis前缀
     */
    String REDIS_PREFIX = "oms:";


    Integer LOG_LEN = 100;

    String REQUEST_LOG = REDIS_PREFIX + "request:log";

    /**
     * 网络请求日记key
     */
    String LIST_NETWORK_LOG = REDIS_PREFIX + "list:network:log";


    /**
     * 默认用户
     */
    String ADMIN_USER_NAME = "admin";

    /**
     * 默认仓库编码
     */
    Long WAREHOUSE_ID = 1L;


}
