package com.example.common.constants;

/**
 * @Description Redis 用到的常量
 * @PackagePath com.example.common.constants.RedisConstants
 * @Author YINZHIYU
 * @Date 2020/5/8 13:47
 * @Version 1.0.0.0
 **/
public interface RedisConstants {
    /**
     * 按天日志
     */
    String LOGS_FORMAT = "yyyy-MM-dd";

    /**
     * 前缀
     */
    String PREFIX = "EXAMPLE:";

    /**
     * 日志
     */
    String LOGS = "LOGS:";

    /**
     * 日志到期天数
     */
    Integer LOGS_EXPIRE_DAYS = 7;

    /**
     * 系统日志
     */
    String SYS_LOGS = PREFIX + LOGS;

    /**
     * 配置信息的缓存
     */
    String CONFIG_CACHE = "CONFIG_CACHE:";

    /**
     * Job 调度配置map key
     */
    String SYS_JOB_CONFIG_MAP_KEY = PREFIX + CONFIG_CACHE + "SYS_JOB_CONFIG_MAP";

    /**
     * 基础配置map key
     */
    String SYS_BASE_CONFIG_MAP_KEY = PREFIX + CONFIG_CACHE + "SYS_BASE_CONFIG_MAP";

}
