package com.example.service.job;

import cn.hutool.core.util.RandomUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @Description 基础JOB
 * @PackagePath BaseSimpleJob
 * @Author YINZHIYU
 * @Date 2020-04-14 14:07:00
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public abstract class BaseSimpleJob implements SimpleJob {

    protected abstract void executeJob(ShardingContext shardingContext);

    @Override
    public void execute(ShardingContext shardingContext) {
        String radomStr = RandomUtil.randomString(6);

        try {
            log.info(String.format("==> [%s]Thread ID: %s, Job 开始工作", radomStr, Thread.currentThread().getId()));
            log.info(String.format("==> [%s]Thread ID: %s, Job 任务总片数: %s, 当前分片项: %s, 当前参数: %s, 当前任务名称: %s",
                    radomStr,
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1)
            ));

            // 程序开始时间
            Instant startTime = Instant.now();

//            if (StringUtils.notBlank(shardingContext.getShardingParameter())) {
            executeJob(shardingContext);
//            } else {
//                log.info(String.format("==> [%s]Thread ID: %s, Job 无工作参数，放弃本次工作。", radomStr, Thread.currentThread().getId()));
//            }

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            log.info(String.format("==> [%s]Thread ID: %s, Job 结束工作，执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), millis));

        } catch (Exception e) {
            log.error(String.format("==> [%s]Thread ID: %s, Job 工作异常 ==> %s", radomStr, Thread.currentThread().getId(), e));
        }
    }
}
