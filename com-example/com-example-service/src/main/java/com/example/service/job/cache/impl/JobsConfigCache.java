package com.example.service.job.cache.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.example.common.annotations.CronExpression;
import com.example.common.annotations.ShardingItemParams;
import com.example.common.annotations.ShardingTotalCount;
import com.example.common.config.JobsConfig;
import com.example.common.constants.JobsConstants;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.JobStatusEnum;
import com.example.common.utils.ClassUtils;
import com.example.common.utils.LogUtils;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.entity.SysJobConfig;
import com.example.service.business.SysJobConfigService;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.cache.BaseCache;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 系统启动中加载jobs配置, 且初始化job
 * @PackagePath cn.wangoon.config.JobsConfigCache
 * @Author YINZHIYU
 * @Date 2020-04-13 09:55:00
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class JobsConfigCache implements BaseCache {

    @Resource
    JobsConfig jobsConfig;

    @Resource
    SysJobConfigService sysJobConfigService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * Job 调度map，便于管理启停
     * KEY -> BeanName  Value JobScheduler
     */
    public final static Map<String, JobScheduler> jobSchedulerMap = new HashMap<>();
    /**
     * Job 调度配置map，便于管理状态
     * KEY -> BeanName  Value SysJobConfig
     */
    public final static Map<String, SysJobConfig> sysJobConfigMap = new HashMap<>();

    @Override
    public void init() {
        LogUtils.info("加载JobSchedulerMap");
//        List<SysJobConfig> sysJobConfigs = sysJobConfigService.list();
//        startJobs(sysJobConfigs);

        startJobs();
    }

    /*
     * @Description 添加job
     * @Params ==>
     * @Param jobClassBeanName       job bean名称
     * @Param cron                   表达式
     * @Param shardingTotalCount     分片数
     * @Param shardingItemParameters 分片参数
     * @Return
     * @Date 2020/4/13 11:14
     * @Auther YINZHIYU
     */
    private JobScheduler createJobScheduler(String jobClassBeanName, String cron, int shardingTotalCount, String shardingItemParameters) {

        JobScheduler jobScheduler = null;
        try {
            SimpleJob job = (SimpleJob) SpringBootBeanUtil.getBean(jobClassBeanName);
            if (ObjectUtil.isEmpty(job)) {
                LogUtils.error(String.format("创建Job ==> 获取Job Bean 失败 ==> SpringBoot 并没有配置名为 %s 的Bean", jobClassBeanName));
                return jobScheduler;
            }
            jobScheduler = jobsConfig.addSimpleJobScheduler(job, cron, shardingTotalCount, shardingItemParameters);
        } catch (Exception e) {
            LogUtils.error("创建Job异常", e);
        }
        return jobScheduler;
    }

    /*
     * @Description 启动定时任务
     * @Params ==>
     * @Param sysJobConfigs
     * @Return
     * @Date 2020/4/13 11:12
     * @Auther YINZHIYU
     */
    private void startJobs(List<SysJobConfig> sysJobConfigs) {

        for (SysJobConfig sysJobConfig : sysJobConfigs) {
            JobScheduler jobScheduler = createJobScheduler(sysJobConfig.getJobClassBeanName(), sysJobConfig.getCronExpression(), sysJobConfig.getShardingTotalCount(), sysJobConfig.getShardingItemParams());
            if (ObjectUtil.isNotEmpty(jobScheduler)) {
                jobScheduler.init();
                jobScheduler.getSchedulerFacade().registerStartUpInfo(JobStatusEnum.getStart(sysJobConfig.getJobStatus()));//是否启动
                jobSchedulerMap.put(sysJobConfig.getJobClassBeanName(), jobScheduler);//便于管理JOB启停
                sysJobConfigMap.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);//便于管理JOB状态
            }
        }

        try {
            redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap);
        } catch (Exception e) {
            LogUtils.error(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigs, "redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap) ==> 操作Redis ==> 异常", e);
        }
    }

    /*
     * @Description 启动指定包路径下的定时任务
     * @Params ==>
     * @Param scanPackage 包路径
     * @Return void
     * @Date 2020/6/16 18:00
     * @Auther YINZHIYU
     */
    private void startJobs() {
        try {

            //获取所有定义的job
            Map<String, Class<?>> stringClassMap = Maps.newHashMap();
            stringClassMap.putAll(ClassUtils.getSuperClassMapForComponent(BaseSimpleJob.class));

            //获取数据库已经配置的job
            List<SysJobConfig> sysJobConfigs = sysJobConfigService.list();
            Map<String, SysJobConfig> jobConfigMap = ObjectUtil.isNotEmpty(sysJobConfigs) ? sysJobConfigs.stream().collect(Collectors.toMap(SysJobConfig::getJobClassBeanName, Function.identity(), (v1, v2) -> v1)) : Maps.newHashMap();

            //遍历所有定义的job
            for (Map.Entry<String, Class<?>> stringClassEntry : stringClassMap.entrySet()) {

                //判断数据库是否配置job
                SysJobConfig sysJobConfig = jobConfigMap.get(stringClassEntry.getKey());

                //若无配置，则配置
                if (ObjectUtil.isEmpty(sysJobConfig)) {
                    Description description = stringClassEntry.getValue().getAnnotation(Description.class);
                    ShardingItemParams shardingItemParams = stringClassEntry.getValue().getAnnotation(ShardingItemParams.class);
                    ShardingTotalCount shardingTotalCount = stringClassEntry.getValue().getAnnotation(ShardingTotalCount.class);
                    CronExpression cronExpression = stringClassEntry.getValue().getAnnotation(CronExpression.class);

                    //新增job的数据库配置
                    sysJobConfig = new SysJobConfig();
                    sysJobConfig.setJobName(stringClassEntry.getKey());
                    sysJobConfig.setJobClassBeanName(stringClassEntry.getKey());
                    sysJobConfig.setShardingTotalCount(ObjectUtil.isNotEmpty(shardingTotalCount) ? shardingTotalCount.value() : 1);
                    sysJobConfig.setShardingItemParams(ObjectUtil.isNotEmpty(shardingItemParams) ? shardingItemParams.value() : "系统自动填入，Job中未配置分片参数注解，请自行添加分片参数");
                    sysJobConfig.setCronExpression(ObjectUtil.isNotEmpty(cronExpression) ? cronExpression.value() : JobsConstants.DEFAULT_CRON_EXPRESSION);
                    sysJobConfig.setRemark(ObjectUtil.isNotEmpty(description) ? description.value() : "系统自动填入，Job中未配置备注描述注解，请自行添加备注");

                    if (StrUtil.equals(JobsConstants.SYSTEM_LISTENER_JOB_CLASS_BEAN_NAME, stringClassEntry.getKey())) {
                        sysJobConfig.setJobStatus(JobStatusEnum.START.getStatus());
                    } else {
                        sysJobConfig.setJobStatus(JobStatusEnum.STOP.getStatus());
                    }

                    sysJobConfig.setCreateUser("Sys");
                    sysJobConfig.setCreateTime(DateUtil.date());
                    sysJobConfig.setUpdateUser("Sys");
                    sysJobConfig.setUpdateTime(DateUtil.date());

                    //数据库新增job配置
                    boolean insertResult = sysJobConfigService.getBaseMapper().insert(sysJobConfig) > 0;
                    if (insertResult) {
                        sysJobConfigs.add(sysJobConfig);//如果是程序新增的JOB，则此处加到内存里，减少再查一次数据库
                    }
                }

                JobScheduler jobScheduler = createJobScheduler(sysJobConfig.getJobClassBeanName(), sysJobConfig.getCronExpression(), sysJobConfig.getShardingTotalCount(), sysJobConfig.getShardingItemParams());
                if (ObjectUtil.isNotEmpty(jobScheduler)) {
                    jobScheduler.init();
                    jobScheduler.getSchedulerFacade().registerStartUpInfo(JobStatusEnum.getStart(sysJobConfig.getJobStatus()));//是否启动
                    jobSchedulerMap.put(sysJobConfig.getJobClassBeanName(), jobScheduler);//便于管理JOB启停
                    sysJobConfigMap.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);//便于管理JOB状态
                }
            }

            //全部初始化好后，再检查并删除需要废弃的Zk中的Job节点(删除数据库需要废弃的节点，在此同步删除Zk中的节点)
            List<String> zkNodes = jobsConfig.getChildNodes("/");//Zk中Oms系统下的所有JOB节点
            List<String> dbNodes = sysJobConfigs.stream().map(SysJobConfig::getJobClassBeanName).collect(Collectors.toList());//数据存在的节点
            List<String> zkDeleteNodes = zkNodes.stream().filter(node -> {
                String temp = node.substring(node.lastIndexOf(".") + 1);
                return !dbNodes.contains(temp);
            }).collect(Collectors.toList());//过滤出在数据库没有的节点
            zkDeleteNodes.forEach(nodePath -> {
                jobsConfig.deleteNode(nodePath);
            });

            try {
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap);
            } catch (Exception e) {
                LogUtils.error(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap, "redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap) ==> 操作Redis ==> 异常", e);
            }

        } catch (Exception e) {
            LogUtils.error("加载启动定时任务异常", e);
        }
    }
}
