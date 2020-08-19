package com.example.common.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * @Description 同步任务状态枚举
 * @Author YINZHIYU
 * @Date 2019-10-09 10:15:00
 * @Version 1.0.0.0
 **/
public enum SyncTaskStatusEnum {
    WAIT("待处理", 0, "#cdccc1"),
    EXCUTING("处理中", 1, "#2404ff"),
    FINISH("已处理", 2, "#079e42"),
    FAIL("处理失败", 3, "#e4d715"),
    EXCEPTION("处理异常", 99, "#f70b0b"),
    STOP("终止", 100, "#3a381d");

    // 成员变量
    private String remark;

    // 类型
    private Integer taskStatus;

    //颜色
    private String color;

    // 构造方法
    SyncTaskStatusEnum(String remark, Integer taskStatus, String color) {
        this.remark = remark;
        this.taskStatus = taskStatus;
        this.color = color;
    }

    public static String getType(Integer taskStatus) {
        for (SyncTaskStatusEnum syncTaskStatusEnum : SyncTaskStatusEnum.values()) {
            if (Objects.equals(syncTaskStatusEnum.getTaskStatus(), taskStatus)) {
                return syncTaskStatusEnum.getRemark();
            }
        }
        return StrUtil.EMPTY;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
