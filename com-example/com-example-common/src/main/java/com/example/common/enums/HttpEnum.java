package com.example.common.enums;

/**
 * @Description HTTP枚举
 * @PackagePath com.example.common.enums.HttpEnum
 * @Author YINZHIYU
 * @Date 2020/5/8 13:47
 * @Version 1.0.0.0
 **/
public enum HttpEnum {
    GET("GET", 1),
    POST("POST", 2),
    PUT("PUT", 3),
    DELETE("DELETE", 4);

    // 成员变量
    private String method;

    // 类型
    private Integer type;

    // 构造方法
    HttpEnum(String method, Integer type) {
        this.method = method;
        this.type = type;
    }

    public static Integer getType(String method) {
        for (HttpEnum httpEnum : HttpEnum.values()) {
            if (method.equals(httpEnum.getMethod())) {
                return httpEnum.getType();
            }
        }
        return 0;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
