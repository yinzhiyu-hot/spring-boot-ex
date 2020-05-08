package com.example.common.enums;

/**
 * @Description JOB状态枚举
 * @PackagePath com.example.common.enums.JobStatusEnum
 * @Author YINZHIYU
 * @Date 2020/5/8 13:47
 * @Version 1.0.0.0
 **/
public enum JobStatusEnum {
    STOP("停止", 0, false),
    START("启动", 1, true);

    // 成员变量
    private String remark;

    // 订单状态
    private Integer status;

    //是否启动 true 是 false 否
    private Boolean isStart;

    // 构造方法
    JobStatusEnum(String remark, Integer status, Boolean isStart) {
        this.remark = remark;
        this.status = status;
        this.isStart = isStart;
    }

    public static String getRemark(Integer status) {
        for (JobStatusEnum jobStatusEnum : JobStatusEnum.values()) {
            if (jobStatusEnum.getStatus().equals(status)) {
                return jobStatusEnum.getRemark();
            }
        }
        return "";
    }

    public static Boolean getStart(Integer status) {
        for (JobStatusEnum jobStatusEnum : JobStatusEnum.values()) {
            if (jobStatusEnum.getStatus().equals(status)) {
                return jobStatusEnum.getStart();
            }
        }
        return false;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }
}
