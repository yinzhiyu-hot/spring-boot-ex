package com.example.common.constants;

/**
 * @Description Job 用到的常量
 * @PackagePath com.example.common.constants.JobsConstants
 * @Author YINZHIYU
 * @Date 2020/5/8 13:46
 * @Version 1.0.0.0
 **/
public interface JobsConstants {
    /**
     * Jobs 参数
     */
    String SHARDING_PARAM_TASK_TYPE = "taskType";

    String SYSTEM_LISTENER_JOB_CLASS_BEAN_NAME = "SystemListenerJob";

    Integer RETRY_COUNT = 2;//重试次数
}
