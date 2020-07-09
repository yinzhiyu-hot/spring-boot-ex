package com.example.service.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.example.common.constants.JobsConstants;
import com.example.common.constants.SysBaseConfigConstants;
import com.example.common.enums.SyncTaskStatusEnum;
import com.example.common.utils.LogUtils;
import com.example.common.utils.StringUtils;
import com.example.domain.common.ShardingItemParameters;
import com.example.domain.dto.SyncTaskDto;
import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.example.domain.entity.SyncTaskException;
import com.example.domain.entity.SysJobConfig;
import com.example.domain.query.SysJobConfigQuery;
import com.example.service.business.SyncTaskDataService;
import com.example.service.business.SyncTaskExceptionService;
import com.example.service.business.SyncTaskService;
import com.example.service.business.SysJobConfigService;
import com.example.service.job.cache.SysBaseConfigCache;
import com.example.service.job.factory.DataSyncFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @Description 基础JOB
 * @PackagePath cn.wangoon.service.job.BaseSimpleJob
 * @Author YINZHIYU
 * @Date 2020-04-14 14:07:00
 * @Version 1.0.0.0
 **/
@Component
public abstract class BaseSimpleJob implements SimpleJob {

    @Resource
    private SyncTaskService syncTaskService;

    @Resource
    private SyncTaskDataService syncTaskDataService;

    @Resource
    private SyncTaskExceptionService syncTaskExceptionService;

    @Resource
    private SysJobConfigService sysJobConfigService;

    protected abstract void executeJob(ShardingContext shardingContext);

    @Override
    public void execute(ShardingContext shardingContext) {
        String radomStr = RandomUtil.randomString(6);
        String jobName = shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1);
        try {
            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 开始工作", radomStr, Thread.currentThread().getId()));
            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 任务总片数: %s, 当前分片项: %s, 当前参数: %s, 当前任务名称: %s",
                    radomStr,
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1)
            ));
            //心跳记录
            heatRecord(jobName, radomStr);

            // 程序开始时间
            Instant startTime = Instant.now();

            // 执行工作
            executeJob(shardingContext);

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 结束工作，执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), millis));

        } catch (Exception e) {
            LogUtils.error(shardingContext, String.format("[%s]Thread ID: %s, Job 工作异常", radomStr, Thread.currentThread().getId()), e);
        }
    }

    /*
     * @Description 开始执行任务
     * @Remark
     * @Params ==>
     * @Param shardingContext
     * @Param dataSyncFactory
     * @Return void
     * @Date 2020/7/9 14:25
     * @Auther YINZHIYU
     */
    public void startExecuteJob(ShardingContext shardingContext, DataSyncFactory dataSyncFactory) {
        //根据分片规则取任务数据
        SyncTaskDto syncTaskDto = new SyncTaskDto();

        //参数从数据库配置而来
        ShardingItemParameters shardingItemParameters = new ShardingItemParameters(shardingContext.getShardingParameter());

        if (ObjectUtil.isEmpty(shardingItemParameters.getMap().get(JobsConstants.SHARDING_PARAM_TASK_TYPE))) {
            return;
        }
        syncTaskDto.setRetryCount(getRetryCountFromGlobalMap());
        syncTaskDto.setTaskType(shardingItemParameters.getMap().get(JobsConstants.SHARDING_PARAM_TASK_TYPE));

        //获取执行任务
        List<SyncTask> syncTaskList = syncTaskService.getProcessTaskList(syncTaskDto);

        if (ObjectUtil.isEmpty(syncTaskList)) {
            return;
        }

        for (SyncTask syncTask : syncTaskList) {
            try {
                //开始处理，更新状态处理中
                syncTask.setProcessCount(syncTask.getProcessCount() + 1);
                syncTask.setTaskStatus(SyncTaskStatusEnum.EXCUTING.getTaskStatus());
                syncTaskService.updateById(syncTask);

                //获取执行任务对应的业务数据，并进行重组
                QueryWrapper<SyncTaskData> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(SyncTaskData.COL_TASK_ID, syncTask.getId());
                queryWrapper.orderByAsc(SyncTaskData.COL_DATA_INDEX);
                List<SyncTaskData> syncTaskDataList = syncTaskDataService.getBaseMapper().selectList(queryWrapper);
                StringBuilder caclueData = new StringBuilder();
                for (SyncTaskData syncTaskData : syncTaskDataList) {
                    caclueData.append(syncTaskData.getTaskData());
                }

                //解码
                String taskData = StringUtils.base64Decode(caclueData.toString());

                //容错处理
                if (ObjectUtil.isEmpty(taskData)) {
                    continue;
                }
                syncTask.setSyncTaskDataList(syncTaskDataList);
                syncTask.setTaskData(taskData);

                //根据task_type 进行业务路由，由不同的入口进行业务处理
                boolean excuteResult = dataSyncFactory.createDataSync().excute(syncTask);

                if (!excuteResult) {
                    //最后一次重试失败，则更新处理失败
                    if (syncTask.getProcessCount() >= getRetryCountFromGlobalMap()) {
                        syncTask.setTaskStatus(SyncTaskStatusEnum.FAIL.getTaskStatus());
                        syncTaskService.updateById(syncTask);
                    }
                    continue;
                }
                //结束处理，更新状态已处理
                syncTask.setTaskStatus(SyncTaskStatusEnum.FINISH.getTaskStatus());
                syncTask.setFinishDate(new Date());
                syncTaskService.updateById(syncTask);
            } catch (Exception ex) {
                //开始处理，更新状态处理中
                syncTask.setTaskStatus(SyncTaskStatusEnum.EXCEPTION.getTaskStatus());
                syncTaskService.updateById(syncTask);

                String exStr = String.format("BaseSimpleJob ==> execute ==> 任务处理异常：%s", ex);
                SyncTaskException syncTaskException = new SyncTaskException();
                syncTaskException.setTaskId(syncTask.getId());
                syncTaskException.setTaskException(exStr.length() > 2000 ? exStr.substring(0, 2000) : exStr);
                syncTaskExceptionService.getBaseMapper().insert(syncTaskException);
            }
        }
    }

    /*
     * @Description 获取任务同步重试次数配置
     * @Params ==>
     * @Return java.lang.Integer
     * @Date 2020/6/12 12:08
     * @Auther YINZHIYU
     */
    protected Integer getRetryCountFromGlobalMap() {
        return SysBaseConfigCache.getSysBaseConfigFromGlobalMap(SysBaseConfigConstants.RETRY_COUNT, SysBaseConfigConstants.RETRY_COUNT_SYNC_TASK, JobsConstants.RETRY_COUNT);
    }

    /*
     * @Description 心跳记录[Job执行一次后记录一次心跳]
     * 备注：此处记录可以放到作业监听中去做，创建监听实现ElasticJobListener，在创建调度的时候加入监听即可，我比较懒，就不自己新写监听了
     * @Params ==>
     * @Param jobName
     * @Return void
     * @Date 2020/6/16 11:15
     * @Auther YINZHIYU
     */
    protected void heatRecord(String jobName, String radomStr) {

        boolean recordResult = false;
        try {
            SysJobConfigQuery sysJobConfigQuery = new SysJobConfigQuery();
            sysJobConfigQuery.setJobName(jobName);
            SysJobConfig sysJobConfig = sysJobConfigService.getSysJobConfigByCondition(sysJobConfigQuery);

            if (ObjectUtil.isNotEmpty(sysJobConfig)) {
                SysJobConfig sysJobConfigHeartRecord = new SysJobConfig();
                sysJobConfigHeartRecord.setId(sysJobConfig.getId());
                sysJobConfigHeartRecord.setUpdateTime(DateUtil.date());
                sysJobConfigHeartRecord.setUpdateUser(jobName);
                recordResult = sysJobConfigService.updateById(sysJobConfigHeartRecord);
            }
        } catch (Exception e) {
            LogUtils.error(jobName, radomStr, String.format("[%s]Thread ID: 心跳记录异常", Thread.currentThread().getId()), e);
        }
        LogUtils.info(jobName, radomStr, String.format("[%s]Thread ID: %s, %s 心跳记录 %s。", radomStr, Thread.currentThread().getId(), jobName, recordResult ? "成功" : "失败"));
    }
}
