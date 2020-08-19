package com.example.service.job.sync.example.biz.impl;

import cn.hutool.json.JSONUtil;
import com.example.domain.entity.SyncTask;
import com.example.service.job.sync.example.biz.ExampleDataSyncService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @Description 数据同步处理业务处理实现bean
 * @PackagePath com.example.service.job.sync.biz.example.impl.ExampleDataSyncServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 14:28
 * @Version 1.0.0.0
 **/
@Component
public class ExampleDataSyncServiceImpl implements ExampleDataSyncService {

    private final static SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /*
     * @Description 同步测试
     * @Params ==>
     * @Param syncTask
     * @Return boolean
     * @Date 2020/4/26 18:02
     * @Auther YINZHIYU
     */
    @Override
    public boolean syncTest(SyncTask syncTask) {
        System.out.println("ExampleDataSyncServiceImpl -> syncTest -> " + JSONUtil.toJsonStr(syncTask));
        System.out.println("ExampleDataSyncServiceImpl -> syncTest -> bizData -> " + syncTask.getTaskData());
        System.out.println("ExampleDataSyncServiceImpl -> syncTest -> bizData -> " + syncTask.getSyncTaskDataList().size());
        return true;
    }
}
