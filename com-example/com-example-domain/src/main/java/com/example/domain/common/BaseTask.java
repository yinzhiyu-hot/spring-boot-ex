package com.example.domain.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 基础单据bean
 * @Remark
 * @PackagePath com.example.domain.common.BaseOrder
 * @Author YINZHIYU
 * @Date 2020/8/19 15:31
 * @Version 1.0.0.0
 **/
@Data
public class BaseTask implements Serializable {
    public String bizDescription;
}
