package com.example.common.enums;

/**
 * @Description 同步任务类型枚举
 * @PackagePath com.example.common.enums.SyncTaskTypeEnum
 * @Author YINZHIYU
 * @Date 2020/5/8 13:48
 * @Version 1.0.0.0
 **/
public enum SyncTaskTypeEnum {
    SYNC_TEST("同步测试", "SYNC_TEST", "syncTest"),
    SYNC_TEST2("同步测试2", "SYNC_TEST2", "syncTest");

    // 成员变量
    private String remark;

    // 任务类型
    private String syncTaskType;

    // 调度中配置的业务bean 对应的处理方法
    private String method;

    // 构造方法
    SyncTaskTypeEnum(String remark, String syncTaskType, String method) {
        this.remark = remark;
        this.syncTaskType = syncTaskType;
        this.method = method;
    }

    public static String getRemark(String syncTaskType) {
        for (SyncTaskTypeEnum syncTaskTypeEnum : SyncTaskTypeEnum.values()) {
            if (syncTaskTypeEnum.getSyncTaskType().equals(syncTaskType)) {
                return syncTaskTypeEnum.getRemark();
            }
        }
        return "";
    }

    public static String getMethod(String syncTaskType) {
        for (SyncTaskTypeEnum syncTaskTypeEnum : SyncTaskTypeEnum.values()) {
            if (syncTaskTypeEnum.getSyncTaskType().equals(syncTaskType)) {
                return syncTaskTypeEnum.getMethod();
            }
        }
        return "";
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSyncTaskType() {
        return syncTaskType;
    }

    public void setSyncTaskType(String syncTaskType) {
        this.syncTaskType = syncTaskType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
