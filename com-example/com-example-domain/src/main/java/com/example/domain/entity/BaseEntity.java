package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 基础Entity
 * @PackagePath com.example.domain.entity.BaseEntity
 * @Author YINZHIYU
 * @Date 2020/5/8 13:54
 * @Version 1.0.0.0
 **/
@Data
public class BaseEntity {


    /**
     * 创建人id
     */
    @TableField(value = "create_by_id", fill = FieldFill.INSERT)
    protected Long createById;
    /**
     * 创建人名称
     */
    @TableField(value = "create_by_name", fill = FieldFill.INSERT)
    protected String createByName;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改人id
     */
    @TableField(value = "modify_by_id", fill = FieldFill.UPDATE)
    protected Long modifyById;
    /**
     * 修改人名称
     */
    @TableField(value = "modify_by_name", fill = FieldFill.UPDATE)
    protected String modifyByName;
    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE, update = "NOW()")
    protected LocalDateTime modifyTime;

    /**
     * 0-正常,1-删除
     */
    @TableLogic // 逻辑删除标识
    @TableField(value = "del_flag", select = false) // 查询的时候不显示
    protected Integer delFlag;
}
