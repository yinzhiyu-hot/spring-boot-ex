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
     * Job 调度配置map key
     */
    String SYS_JOB_CONFIG_MAP_KEY = "SYS_JOB_CONFIG_MAP";

    /**
     * 仓库库存分配锁
     */
    String WAREHOUSE_INVENTORY_ALLOCATION_LOCK_KEY = "WAREHOUSE_INVENTORY_ALLOCATION_LOCK_KEY";

}
