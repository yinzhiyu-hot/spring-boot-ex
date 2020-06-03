package com.example.service.job.listener;

import cn.hutool.core.util.ObjectUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.JobStatusEnum;
import com.example.common.utils.NetUtils;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.entity.SysBaseConfig;
import com.example.domain.entity.SysJobConfig;
import com.example.service.business.SysJobConfigService;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.cache.JobsConfigCache;
import com.example.service.job.cache.SysBaseConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 系统监听JOB
 * @PackagePath com.example.service.job.listener.SystemListenerJob
 * @Author YINZHIYU
 * @Date 2020/6/3 16:06
 * @Version 1.0.0.0
 **/
@Slf4j
@Component("SystemListenerJob")
@DisallowConcurrentExecution
public class SystemListenerJob extends BaseSimpleJob {

    @Resource
    Environment environment;

    @Resource
    RedisUtils redisUtils;

    @Resource
    public SysBaseConfigCache sysBaseConfigCache;

    @Resource
    private SysJobConfigService sysJobConfigService;

    @Override
    protected void executeJob(ShardingContext shardingContext) {

        //同步job配置
        syncSysJobConfigCache();

        //同步基础配置
        syncSysBaseConfigCache();
    }

    /*
     * @Description 同步JOB配置 [多实例部署时，监控其它实例是否有更新配置，有则同步]
     * @Params ==>
     * @Return void
     * @Date 2020/5/22 9:25
     * @Auther YINZHIYU
     */
    private void syncSysJobConfigCache() {
        try {
            Map<String, SysJobConfig> sysJobConfigMapLocal = JobsConfigCache.sysJobConfigMap;//取本地 JOB 调度配置

            //获取Redis 中 SysJobConfig，管理jobScheduler的启停
            Map<String, SysJobConfig> sysJobConfigMapRedis = null;//取Redis 调度配置map

            try {
                sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);
            } catch (Exception e) {
                log.error(String.format("SystemListenerJob ==> syncSysJobConfigCache ==> 操作Redis ==> 异常：%s", e));
            }

            if (ObjectUtil.isEmpty(sysJobConfigMapRedis)) {
                return;
            }

            for (Map.Entry<String, SysJobConfig> entry : sysJobConfigMapRedis.entrySet()) {

                //找不到
                if (ObjectUtil.isEmpty(sysJobConfigMapLocal.get(entry.getKey()))) {
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

                    log.info(String.format("SystemListenerJob ==> syncSysJobConfigCache ==> 心跳状态监测Job ==> Job:%s ==> 执行 %s", entry.getKey(), JobStatusEnum.getRemark(entry.getValue().getJobStatus())));
                }

                //如果是更新配置操作
                if (entry.getValue().isUpdateFlag() && !getLocalIpPort().equals(entry.getValue().getUpdateIpPort())) {
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
                        redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
                    } catch (Exception e) {
                        log.error(String.format("SystemListenerJob ==> syncSysJobConfigCache ==> 操作Redis ==> 异常：%s", e));
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("SystemListenerJob ==> syncSysJobConfigCache ==> 异常：%s", e));
        }
    }

    /*
     * @Description 同步基础配置 [多实例部署时，监控其它实例是否有更新配置，有则同步]
     * @Params ==>
     * @Return void
     * @Date 2020/5/22 9:25
     * @Auther YINZHIYU
     */
    private void syncSysBaseConfigCache() {
        try {
            //获取Redis 中 sysBaseConfig 存储的操作[更新/删除/新增]信息
            SysBaseConfig sysBaseConfig = null;//取Redis 基础配置

            try {
                sysBaseConfig = redisUtils.getValue(RedisConstants.SYS_BASE_CONFIG_MAP_KEY);
            } catch (Exception e) {
                log.error(String.format("SystemListenerJob ==> syncSysBaseConfigCache ==> 操作Redis ==> 异常：%s", e));
            }

            if (ObjectUtil.isEmpty(sysBaseConfig)) {
                return;
            }

            //如果存在更新/删除/新增配置操作
            if (sysBaseConfig.isUpdateFlag() && !getLocalIpPort().equals(sysBaseConfig.getUpdateIpPort())) {

                //重新从数据库加载基础配置
                sysBaseConfigCache.init();

                //更新Redis 的更新标识
                sysBaseConfig.setUpdateFlag(false);
                try {
                    redisUtils.set(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig);
                } catch (Exception e) {
                    log.error(String.format("SystemListenerJob ==> syncSysBaseConfigCache ==> 操作Redis ==> 异常：%s", e));
                }
            }
        } catch (Exception e) {
            log.error(String.format("SystemListenerJob ==> syncSysBaseConfigCache ==> 异常：%s", e));
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
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(
                        JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(), jobClass.getCanonicalName()
                )
        ).overwrite(true).build();
    }

    /*
     * @Description 获取Ip+端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:13
     * @Auther YINZHIYU
     */
    private String getLocalIpPort() {
        return String.format("%s:%s", NetUtils.getLocalIP(), getLocalPort());
    }

    /*
     * @Description 获取应用端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:11
     * @Auther YINZHIYU
     */
    private String getLocalPort() {
        return environment.getProperty("local.server.port");
    }
}
