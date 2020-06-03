package com.example.service.job.sync.biz.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.example.common.constants.JobsConstants;
import com.example.common.enums.SyncTaskStatusEnum;
import com.example.common.utils.StringUtils;
import com.example.domain.common.ShardingItemParameters;
import com.example.domain.dto.SyncTaskDto;
import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.example.domain.entity.SyncTaskException;
import com.example.service.business.SyncTaskDataService;
import com.example.service.business.SyncTaskExceptionService;
import com.example.service.business.SyncTaskService;
import com.example.service.job.BaseSimpleJob;
import com.example.service.job.factory.example.ExampleDataSyncFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    @Resource
    private SyncTaskService syncTaskService;
    @Resource
    private SyncTaskDataService syncTaskDataService;
    @Resource
    private SyncTaskExceptionService syncTaskExceptionService;

    @Override
    public void executeJob(ShardingContext shardingContext) {
        //根据分片规则取任务数据
        SyncTaskDto syncTaskDto = new SyncTaskDto();

        //参数从数据库配置而来
        ShardingItemParameters shardingItemParameters = new ShardingItemParameters(shardingContext.getShardingParameter());

        if (StringUtils.isBlank(shardingItemParameters.getMap().get(JobsConstants.SHARDING_PARAM_TASK_TYPE))) {
            return;
        }

        syncTaskDto.setTaskType(shardingItemParameters.getMap().get(JobsConstants.SHARDING_PARAM_TASK_TYPE));

        //获取执行任务
        List<SyncTask> syncTaskList = syncTaskService.getProcessTaskList(syncTaskDto);

        if (syncTaskList == null || syncTaskList.size() < 1) {
            return;
        }

        for (SyncTask syncTask : syncTaskList) {
            try {
                //开始处理，更新状态处理中
                syncTask.setProcessCount(syncTask.getProcessCount() + 1);
                syncTask.setTaskStatus(SyncTaskStatusEnum.EXCUTING.getTaskStatus());
                syncTaskService.updateById(syncTask);

                //获取执行任务对应的业务数据，并进行重组
                QueryWrapper<SyncTaskData> queryWrapper = new QueryWrapper<SyncTaskData>();
                queryWrapper.eq("task_id", syncTask.getId());
                queryWrapper.orderByAsc("data_index");
                List<SyncTaskData> syncTaskDataList = syncTaskDataService.getBaseMapper().selectList(queryWrapper);
                StringBuilder caclueData = new StringBuilder();
                for (SyncTaskData syncTaskData : syncTaskDataList) {
                    caclueData.append(syncTaskData.getTaskData());
                }

                //解码
                String taskData = StringUtils.base64Decode(caclueData.toString());

                //容错处理
                if (StringUtils.isBlank(taskData)) {
                    continue;
                }
                syncTask.setSyncTaskDataList(syncTaskDataList);
                syncTask.setTaskData(taskData);

                //根据task_type 进行业务路由，由不同的入口进行业务处理
                boolean excuteResult = exampleDataSyncFactory.createDataSync().excute(syncTask);

                if (!excuteResult) {
                    //最后一次重试失败，则更新处理失败
                    if (syncTask.getProcessCount() >= 5) {
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

                String exStr = String.format("ExampleSyncTaskJob ==> execute ==> 任务处理异常：%s", ex);
                SyncTaskException syncTaskException = new SyncTaskException();
                syncTaskException.setTaskId(syncTask.getId());
                syncTaskException.setTaskException(exStr.length() > 2000 ? exStr.substring(0, 2000) : exStr);
                syncTaskExceptionService.getBaseMapper().insert(syncTaskException);
            }
        }
    }
}