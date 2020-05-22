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
     * 前缀
     */
    String PREFIX = "EXAMPLE:";

    /**
     * 日志
     */
    String LOGS = "LOGS:";

    /**
     * Job 调度配置map key
     */
    String SYS_JOB_CONFIG_MAP_KEY = PREFIX + "SYS_JOB_CONFIG_MAP";

    /**
     * 基础配置map key
     */
    String SYS_BASE_CONFIG_MAP_KEY = PREFIX + "SYS_BASE_CONFIG_MAP";

}
