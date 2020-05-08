package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 同步任务数据
 * @PackagePath com.example.domain.entity.SyncTaskData
 * @Author YINZHIYU
 * @Date 2020/5/8 13:54
 * @Version 1.0.0.0
 **/
@Data
@TableName("sync_task_data")
public class SyncTaskData implements Serializable {

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
     * 任务数据
     */
    @TableField("task_data")
    private String taskData;

    /**
     * 截断顺序
     */
    @TableField("data_index")
    private Integer dataIndex;
}
