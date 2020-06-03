package com.example.service.job.factory;

import cn.hutool.core.util.RandomUtil;
import com.example.domain.entity.SyncTask;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @Description 数据同步
 * @PackagePath com.example.service.job.factory.DataSync
 * @Author YINZHIYU
 * @Date 2020/5/8 14:10
 * @Version 1.0.0.0
 **/
@Slf4j
public abstract class DataSync {

    /**
     * 数据同步路由适配方法
     *
     * @param syncTask
     * @return
     */
    protected abstract boolean adapter(SyncTask syncTask) throws Exception;

    /**
     * 数据同步执行方法
     *
     * @param syncTask
     * @return
     */
    public boolean excute(SyncTask syncTask) {

        boolean excuteResult = false;

        String radomStr = RandomUtil.randomString(6);

        try {

            // 程序开始时间
            Instant startTime = Instant.now();

            excuteResult = adapter(syncTask);

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            log.info(String.format("==> [%s]Thread ID: %s, DataSync 数据同步结束, 同步 %s, 执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), excuteResult ? "成功" : "失败", millis));

        } catch (Exception e) {
            throw new RuntimeException(String.format("[%s]Thread ID: %s, DataSync 数据同步异常 ==> %s", radomStr, Thread.currentThread().getId(), e));
        }
        return excuteResult;
    }
}
