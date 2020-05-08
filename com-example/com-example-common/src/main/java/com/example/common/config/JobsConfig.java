package com.example.common.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Description 将zk  cron  job 建立关系
 * @PackagePath com.example.common.config.JobsConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:45
 * @Version 1.0.0.0
 **/
@Configuration
public class JobsConfig {

    @Resource
    private ZookeeperRegistryCenter regCenter;

    public JobsConfig() {
    }

    /**
     * 添加job
     *
     * @param simpleJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    public JobScheduler addSimpleJobScheduler(final SimpleJob simpleJob, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return new SpringJobScheduler(simpleJob, regCenter, getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
    }


    /**
     * @Description 任务配置类
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {


        return LiteJobConfiguration
                .newBuilder(
                        new SimpleJobConfiguration(
                                JobCoreConfiguration.newBuilder(
                                        jobClass.getName(), cron, shardingTotalCount)
                                        .shardingItemParameters(shardingItemParameters)
                                        .build()
                                , jobClass.getCanonicalName()
                        )
                )
                .overwrite(true)
                .build();

    }
}