package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 同步任务
 * @PackagePath com.example.domain.entity.SyncTask
 * @Author YINZHIYU
 * @Date 2020/5/8 13:55
 * @Version 1.0.0.0
 **/
@Data
@TableName("sync_task")
public class SyncTask implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @TableField("id")
    private Integer id;

    /**
     * 任务类型
     */
    @TableField("task_type")
    private String taskType;

    /**
     * 任务描述
     */
    @TableField("task_desc")
    private String taskDesc;

    /**
     * 任务状态(0 待处理 1 处理中 2 已处理 99 处理异常)
     */
    @TableField("task_status")
    private Integer taskStatus;

    /**
     * 任务执行次数
     */
    @TableField("process_count")
    private Integer processCount;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 完成时间
     */
    @TableField("finish_date")
    private Date finishDate;

    /**
     * 时间戳
     */
    @TableField("ts")
    private Date ts;

    /**
     * 原始数据
     */
    @TableField(exist = false)
    private String taskData;

    /**
     * 业务数据
     */
    @TableField(exist = false)
    private List<SyncTaskData> syncTaskDataList;

    public String getTaskData() {
        StringBuffer taskDataStr = new StringBuffer();
        if (syncTaskDataList != null && syncTaskDataList.size() > 0) {
            List<SyncTaskData> list = syncTaskDataList.stream().sorted(Comparator.comparing(SyncTaskData::getDataIndex)).collect(Collectors.toList());
            list.forEach(item -> {
                taskDataStr.append(item.getTaskData());
            });
        }
        if (StringUtils.notBlank(taskDataStr.toString())) {
            this.taskData = StringUtils.base64Decode(taskDataStr.toString());
        }
        return taskData;
    }
}
