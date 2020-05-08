package com.example.service.job.sync.biz.example;

import com.example.domain.entity.SyncTask;
import org.springframework.stereotype.Component;

/**
 * @Description 数据同步处理业务处理接口bean
 * @PackagePath com.example.service.job.sync.biz.example.ExampleDataSyncService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:28
 * @Version 1.0.0.0
 **/
@Component
public interface ExampleDataSyncService {

    boolean syncTest(SyncTask syncTask);
}
