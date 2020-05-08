package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 基础信息配置实体
 * @PackagePath com.example.domain.entity.SysBaseConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:55
 * @Version 1.0.0.0
 **/
@ApiModel(value = "基础信息配置实体")
@Data
@TableName(value = "sys_base_config")
public class SysBaseConfig {
    /**
     * 基础配置表主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "基础配置表主键")
    private Long id;

    /**
     * 业务类型
     */
    @TableField(value = "biz_type")
    @ApiModelProperty(value = "业务类型")
    private String bizType;

    /**
     * 业务配置键
     */
    @TableField(value = "biz_key")
    @ApiModelProperty(value = "业务配置键")
    private String bizKey;

    /**
     * 业务配置值
     */
    @TableField(value = "biz_value")
    @ApiModelProperty(value = "业务配置值")
    private String bizValue;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 是否删除 0 否 1 是
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "是否删除 0 否 1 是")
    private Integer delFlag;

    /**
     * 时间戳
     */
    @TableField(value = "ts")
    @ApiModelProperty(value = "时间戳")
    private Date ts;

}