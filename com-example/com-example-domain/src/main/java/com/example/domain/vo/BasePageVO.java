package com.example.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分页
 * @PackagePath com.example.domain.entity.UserInfoEntity
 * @Author YINZHIYU
 * @Date 2020-04-07 09:54:00
 * @Version 1.0.0.0
 **/
@Data
public class BasePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 码距
     */
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    private String sortName;

    /**
     * 正序/倒序（asc/desc）
     */
    private String sortOrder;
}
