package com.example.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分页
 * @PackagePath com.example.domain.vo.BasePageVO
 * @Author YINZHIYU
 * @Date 2020/5/8 13:56
 * @Version 1.0.0.0
 **/
@Data
@ApiModel(description = "分页参数")
public class BasePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer pageNumber = 1;

    /**
     * 码距
     */
    @ApiModelProperty(value = "码距")
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String sortName;

    /**
     * 正序/倒序（asc/desc）
     */
    @ApiModelProperty(value = "正序/倒序（asc/desc）")
    private String sortOrder;
}
