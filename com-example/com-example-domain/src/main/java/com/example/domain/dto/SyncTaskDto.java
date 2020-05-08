package com.example.domain.dto;

import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description  同步任务Dto
 * @PackagePath  com.example.domain.dto.SyncTaskDto
 * @Author       YINZHIYU
 * @Date         2020/5/8 13:53
 * @Version      1.0.0.0
 **/
public class SyncTaskDto extends SyncTask {

    private int mol;

    private int molValue;

    private String taskData;

    private List<SyncTaskData> syncTaskDataList = new ArrayList<>();

    public int getMol() {
        return mol;
    }

    public void setMol(int mol) {
        this.mol = mol;
    }

    public int getMolValue() {
        return molValue;
    }

    public void setMolValue(int molValue) {
        this.molValue = molValue;
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }


    public List<SyncTaskData> getSyncTaskDataList() {
        return syncTaskDataList;
    }

    public void setSyncTaskDataList(List<SyncTaskData> syncTaskDataList) {
        this.syncTaskDataList = syncTaskDataList;
    }
}
