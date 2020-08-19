package com.example.service.job.factory.example;

import com.example.common.enums.SyncTaskTypeEnum;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.entity.SyncTask;
import com.example.service.job.factory.DataSync;
import com.example.service.job.sync.example.biz.impl.ExampleDataSyncServiceImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description 数据同步路由适配[根据taskType 调用业务逻辑bean不同方法 -> taskType 对应枚举中method  即方法名]
 * @PackagePath com.example.service.job.factory.example.ExampleDataSync
 * @Author YINZHIYU
 * @Date 2020/5/8 14:13
 * @Version 1.0.0.0
 **/
@Component
public class ExampleDataSync extends DataSync {
    @Override
    protected boolean adapter(SyncTask syncTask) throws Exception {
        Method method = ExampleDataSyncServiceImpl.class.getMethod(SyncTaskTypeEnum.getMethod(syncTask.getTaskType()), SyncTask.class);
        return (boolean) method.invoke(SpringBootBeanUtil.getBean(ExampleDataSyncServiceImpl.class), syncTask);
    }
}
