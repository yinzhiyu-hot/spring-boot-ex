package com.example.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description Redis日志
 * @PackagePath com.example.domain.dto.RedisLogDto
 * @Author YINZHIYU
 * @Date 2020/6/3 15:48
 * @Version 1.0.0.0
 **/
@Data
@ApiModel(description = "Redis日志")
public class RedisLogDto implements Serializable {

    /**
     * hashKey
     */
    @ApiModelProperty(value = "hashKey")
    private String hashKey;

    /**
     * 消息
     */
    @ApiModelProperty(value = "消息")
    private String message;

    /**
     * 记录时间
     */
    @ApiModelProperty(value = "记录时间")
    private Date recordDate;

    /**
     * 查询日期
     */
    @ApiModelProperty(value = "查询日期")
    private String queryDate;

    public RedisLogDto() {
    }

    /**
     * 构造
     *
     * @param message
     */
    public RedisLogDto(String message) {
        this.message = message;
        this.recordDate = new Date();
    }
}
