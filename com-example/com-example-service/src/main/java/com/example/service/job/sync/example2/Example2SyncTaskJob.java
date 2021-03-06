package com.example.service.job.sync.example2;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.example.common.annotations.CronExpression;
import com.example.common.annotations.ShardingItemParams;
import com.example.common.annotations.ShardingTotalCount;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.factory.example2.Example2DataSyncFactory;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 业务数据异步处理job
 * @Remark
 * @PackagePath com.example.service.job.sync.example2.Example2SyncTaskJob
 * @Author YINZHIYU
 * @Date 2020/8/19 14:59
 * @Version 1.0.0.0
 **/
@Component("Example2SyncTaskJob")
@Description("同步Job")
@ShardingTotalCount(1)
@ShardingItemParams("0=taskType:SYNC_TEST2")
@CronExpression("*/10 * * * * ?")
@DisallowConcurrentExecution
public class Example2SyncTaskJob extends BaseSimpleJob {

    @Resource
    Example2DataSyncFactory example2DataSyncFactory;

    @Override
    public void executeJob(ShardingContext shardingContext) {
        super.startExecuteJob(shardingContext, example2DataSyncFactory);
    }
}