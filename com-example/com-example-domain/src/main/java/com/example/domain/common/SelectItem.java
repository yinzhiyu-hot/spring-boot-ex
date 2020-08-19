package com.example.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 下拉框项基础bean
 * @Remark
 * @PackagePath cn.wangoon.domain.common.SelectItem
 * @Author YINZHIYU
 * @Date 2020/7/6 9:54
 * @Version 1.0.0.0
 **/
@ApiModel(description = "下拉框项")
@Data
public class SelectItem implements Serializable {
    @ApiModelProperty(value = "显示文本")
    private String code;

    @ApiModelProperty(value = "值")
    private String value;
}
