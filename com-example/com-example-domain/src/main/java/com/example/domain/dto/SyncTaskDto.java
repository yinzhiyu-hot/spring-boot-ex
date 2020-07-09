package com.example.domain.dto;

import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 同步任务Dto
 * @PackagePath com.example.domain.dto.SyncTaskDto
 * @Author YINZHIYU
 * @Date 2020/5/8 13:53
 * @Version 1.0.0.0
 **/
@Data
public class SyncTaskDto extends SyncTask {

    private int mol;

    private int molValue;

    private String taskData;

    private Integer retryCount;

    private List<SyncTaskData> syncTaskDataList = Lists.newArrayList();
}
