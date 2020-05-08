package com.example.service.job.cache;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.example.common.config.JobsConfig;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.JobStatusEnum;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.entity.SysJobConfig;
import com.example.service.business.SysJobConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 系统启动中加载jobs配置, 且初始化job
 * @PackagePath com.example.service.job.cache.JobsConfigCache
 * @Author YINZHIYU
 * @Date 2020/5/8 14:14
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class JobsConfigCache implements BaseCache {

    @Resource
    private RedisUtils redisUtils;

    /**
     * Job 调度map，便于管理启停
     * KEY -> BeanName  Value JobScheduler
     */
    public final static Map<String, JobScheduler> jobSchedulerMap = new HashMap<String, JobScheduler>();

    /**
     * Job 调度配置map，便于管理状态
     * KEY -> BeanName  Value SysJobConfig
     */
    public final static Map<String, SysJobConfig> sysJobConfigMap = new HashMap<String, SysJobConfig>();

    @Resource
    JobsConfig jobsConfig;

    @Resource
    SysJobConfigService sysJobConfigService;

    @Override
    public void init() {
        log.info("加载JobSchedulerMap");
        List<SysJobConfig> sysJobConfigs = sysJobConfigService.list();
        startJobs(sysJobConfigs);
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

            jobScheduler = jobsConfig.addSimpleJobScheduler(job, cron, shardingTotalCount, shardingItemParameters);
        } catch (Exception e) {
            log.error(String.format("创建job异常 -> %s", e.getMessage()));
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
            if (jobScheduler != null) {
                jobScheduler.init();
                jobScheduler.getSchedulerFacade().registerStartUpInfo(JobStatusEnum.START.getStatus().equals(sysJobConfig.getJobStatus()));//是否启动
                jobSchedulerMap.put(sysJobConfig.getJobClassBeanName(), jobScheduler);//便于管理JOB启停
                sysJobConfigMap.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);//便于管理JOB状态
            }
        }

        try {
            redisUtils.redisTemplate.opsForValue().set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMap);
        } catch (Exception e) {
            log.error(String.format("JobsConfigCache ==> startJobs ==> 操作Redis ==> 异常：%s", e));
        }
    }
}
