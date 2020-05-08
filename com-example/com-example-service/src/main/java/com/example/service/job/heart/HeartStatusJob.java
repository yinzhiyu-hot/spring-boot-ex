package com.example.service.job.heart;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.example.common.constants.JobsConstants;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.JobStatusEnum;
import com.example.common.utils.NetUtils;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.entity.SysJobConfig;
import com.example.service.business.SysJobConfigService;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.cache.JobsConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 心跳状态监测JOB
 * @PackagePath com.example.service.job.heart.HeartStatusJob
 * @Author YINZHIYU
 * @Date 2020/5/8 14:10
 * @Version 1.0.0.0
 **/
@Slf4j
@Component("HeartStatusJob")
@DisallowConcurrentExecution
public class HeartStatusJob extends BaseSimpleJob {

    @Resource
    RedisUtils redisUtils;

    @Resource
    private SysJobConfigService sysJobConfigService;

    @Override
    protected void executeJob(ShardingContext shardingContext) {

        Map<String, SysJobConfig> sysJobConfigMapLocal = JobsConfigCache.sysJobConfigMap;//取本地 JOB 调度配置

        //获取Redis 中 SysJobConfig，管理jobScheduler的启停
        Map<String, SysJobConfig> sysJobConfigMapRedis = null;//取Redis 调度配置map

        try {
            sysJobConfigMapRedis = (Map<String, SysJobConfig>) redisUtils.get(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);
        } catch (Exception e) {
            log.error(String.format("HeartStatusJob ==> executeJob ==> 操作Redis ==> 异常：%s", e));
        }

        if (sysJobConfigMapRedis == null) {
            return;
        }

        for (Map.Entry<String, SysJobConfig> entry : sysJobConfigMapRedis.entrySet()) {

            //找不到，或者是心跳Job则忽略
            if (sysJobConfigMapLocal.get(entry.getKey()) == null || JobsConstants.HEART_JOB_CLASS_BEAN_NAME.equals(sysJobConfigMapLocal.get(entry.getKey()).getJobClassBeanName())) {
                continue;
            }
            //如果本地状态和Redis存放的状态不一致，说明有人启停了job，并更新了Redis的jobs配置
            if (!entry.getValue().getJobStatus().equals(sysJobConfigMapLocal.get(entry.getKey()).getJobStatus())) {

                //管理job启停
                JobsConfigCache.jobSchedulerMap.get(entry.getKey()).getSchedulerFacade().registerStartUpInfo(JobStatusEnum.getStart(entry.getValue().getJobStatus()));

                //更新数据库job 配置状态
                SysJobConfig sysJobConfigLocal = entry.getValue();
                sysJobConfigService.updateById(sysJobConfigLocal);

                //同步更新覆盖本地Job配置map
                JobsConfigCache.sysJobConfigMap.put(entry.getKey(), entry.getValue());

                log.info(String.format("HeartStatusJob ==> executeJob ==> 心跳状态监测Job -> Job:%s -> 执行 %s", entry.getKey(), JobStatusEnum.getRemark(entry.getValue().getJobStatus())));
            }

            //如果是更新配置操作
            if (entry.getValue().isUpdateFlag() && !NetUtils.getLocalIP().equals(entry.getValue().getUpdateIp())) {
                //更新调度配置信息
                SimpleJob syncTaskJob = (SimpleJob) SpringBootBeanUtil.getBean(entry.getKey());
                LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(syncTaskJob.getClass(), entry.getValue().getCronExpression(), entry.getValue().getShardingTotalCount(), entry.getValue().getShardingItemParams());
                JobsConfigCache.jobSchedulerMap.get(entry.getKey()).getSchedulerFacade().updateJobConfiguration(liteJobConfiguration);

                //更新数据库job配置信息
                SysJobConfig sysJobConfigLocal = entry.getValue();
                sysJobConfigService.updateById(sysJobConfigLocal);

                //同步更新覆盖本地Job配置map
                JobsConfigCache.sysJobConfigMap.put(entry.getKey(), entry.getValue());

                //更新Redis 的更新标识
                entry.getValue().setUpdateFlag(false);
                sysJobConfigMapRedis.put(entry.getKey(), entry.getValue());
                try {
                    redisUtils.redisTemplate.opsForValue().set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
                } catch (Exception e) {
                    log.error(String.format("HeartStatusJob ==> executeJob ==> 操作Redis ==> 异常：%s", e));
                }
            }
        }
    }

    /*
     * @Description 任务配置类
     * @Params ==>
     * @Param jobClass
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.config.LiteJobConfiguration
     * @Date 2020/4/27 18:04
     * @Auther YINZHIYU
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
