package com.example.service.job.sync.biz.example;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.factory.example.ExampleDataSyncFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 业务数据异步处理job
 * @PackagePath com.example.service.job.sync.biz.example.ExampleSyncTaskJob
 * @Author YINZHIYU
 * @Date 2020/5/8 14:23
 * @Version 1.0.0.0
 **/
@Slf4j
@Component("ExampleSyncTaskJob")
@DisallowConcurrentExecution
public class ExampleSyncTaskJob extends BaseSimpleJob {

    @Resource
    ExampleDataSyncFactory exampleDataSyncFactory;


    @Override
    public void executeJob(ShardingContext shardingContext) {
        super.startExecuteJob(shardingContext, exampleDataSyncFactory);
    }
}