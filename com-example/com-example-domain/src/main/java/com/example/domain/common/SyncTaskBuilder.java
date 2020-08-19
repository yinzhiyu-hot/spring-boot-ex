package com.example.domain.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.SyncTaskStatusEnum;
import com.example.common.enums.SyncTaskTypeEnum;
import com.example.common.utils.StringUtils;
import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 异步任务生成器
 * @PackagePath cn.wangoon.domain.common.SyncTaskBuilder
 * @Author YINZHIYU
 * @Date 2020/5/29 15:07
 * @Version 1.0.0.0
 **/
@Component
@Scope("prototype")
public class SyncTaskBuilder<T extends BaseTask> {

    /*
     * @Description 批量转换异步任务
     * @Remark
     * @Params ==>
     * @Param tList
     * @Param syncTaskTypeEnum
     * @Return java.util.List<com.example.domain.entity.SyncTask>
     * @Date 2020/8/19 15:33
     * @Auther YINZHIYU
     */
    public List<SyncTask> convertSyncTasks(List<T> tList, SyncTaskTypeEnum syncTaskTypeEnum) {
        List<SyncTask> syncTaskList = Lists.newArrayList();
        for (T t : tList) {
            syncTaskList.add(convertSyncTask(t, syncTaskTypeEnum));
        }
        return syncTaskList;
    }

    /*
     * @Description 单个转换异步任务
     * @Remark
     * @Params ==>
     * @Param t
     * @Param syncTaskTypeEnum
     * @Return com.example.domain.entity.SyncTask
     * @Date 2020/8/19 15:32
     * @Auther YINZHIYU
     */
    public SyncTask convertSyncTask(T t, SyncTaskTypeEnum syncTaskTypeEnum) {

        //组装任务主档
        SyncTask syncTask = new SyncTask();
        syncTask.setTaskType(syncTaskTypeEnum.getSyncTaskType());
        syncTask.setTaskDesc(String.format("%s ==> %s", syncTaskTypeEnum.getRemark(), t.getBizDescription()));
        syncTask.setTaskStatus(SyncTaskStatusEnum.WAIT.getTaskStatus());
        syncTask.setProcessCount(0);
        syncTask.setCreateDate(DateUtil.date());

        //组装任务明细
        syncTask.setSyncTaskDataList(convertSyncTaskDatas(t));

        return syncTask;
    }

    /*
     * @Description 转换明细
     * @Remark
     * @Params ==>
     * @Param t
     * @Return java.util.List<com.example.domain.entity.SyncTaskData>
     * @Date 2020/8/19 15:32
     * @Auther YINZHIYU
     */
    private List<SyncTaskData> convertSyncTaskDatas(T t) {
        String taskData = StringUtils.base64Encode(JSONUtil.toJsonStr(JSONUtil.parseObj(t, true)));
        String[] taskDataArray = StringUtils.splitString(taskData, 2000);//2000长度进行字符串分割
        List<SyncTaskData> syncTaskDataList = Lists.newArrayList();
        for (int i = 0; i < taskDataArray.length; i++) {
            SyncTaskData syncTaskData = new SyncTaskData();
            syncTaskData.setTaskData(taskDataArray[i]);
            syncTaskData.setDataIndex(i);
            syncTaskDataList.add(syncTaskData);
        }
        return syncTaskDataList;
    }
}
