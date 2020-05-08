package com.example.common.enums;

/**
 * @Description 删除标记
 * @PackagePath com.example.common.enums.DelFlagEnum
 * @Author YINZHIYU
 * @Date 2020/5/8 13:47
 * @Version 1.0.0.0
 **/
public enum DelFlagEnum {
    NO("未删除", 0),
    YES("已删除", 1);

    // 成员变量
    private String remark;

    // 删除标记
    private Integer flag;

    // 构造方法
    DelFlagEnum(String remark, Integer flag) {
        this.remark = remark;
        this.flag = flag;
    }

    public static String getRemark(Integer flag) {
        for (DelFlagEnum delFlagEnum : DelFlagEnum.values()) {
            if (delFlagEnum.getFlag().equals(flag)) {
                return delFlagEnum.getRemark();
            }
        }
        return "";
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
