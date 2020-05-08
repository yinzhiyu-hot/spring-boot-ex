package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 任务异常信息
 * @PackagePath com.example.domain.entity.SyncTaskException
 * @Author YINZHIYU
 * @Date 2020/5/8 13:55
 * @Version 1.0.0.0
 **/
@Data
@TableName("sync_task_exception")
public class SyncTaskException implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @TableField("id")
    private Integer id;

    /**
     * 任务表主键
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 异常信息
     */
    @TableField("task_exception")
    private String taskException;
}
