package com.example.common.enums;

/**
 * @Description 同步任务状态枚举
 * @PackagePath com.example.common.enums.SyncTaskStatusEnum
 * @Author YINZHIYU
 * @Date 2020/5/8 13:48
 * @Version 1.0.0.0
 **/
public enum SyncTaskStatusEnum {
    WAIT("待处理", 0),
    EXCUTING("处理中", 1),
    FINISH("已处理", 2),
    FAIL("处理失败", 3),
    EXCEPTION("处理异常", 99);

    // 成员变量
    private String remark;

    // 类型
    private Integer taskStatus;

    // 构造方法
    SyncTaskStatusEnum(String remark, Integer taskStatus) {
        this.remark = remark;
        this.taskStatus = taskStatus;
    }

    public static String getType(Integer taskStatus) {
        for (SyncTaskStatusEnum syncTaskStatusEnum : SyncTaskStatusEnum.values()) {
            if (syncTaskStatusEnum.getTaskStatus().equals(taskStatus)) {
                return syncTaskStatusEnum.getRemark();
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

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }
}
