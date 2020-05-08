package com.example.service.job.factory.example;

import com.example.common.utils.SpringBootBeanUtil;
import com.example.service.job.factory.DataSync;
import com.example.service.job.factory.DataSyncFactory;
import org.springframework.stereotype.Component;

/**
 * @Description  TODO
 * @PackagePath com.example.service.job.factory.example.ExampleDataSyncFactory
 * @Author       YINZHIYU
 * @Date         2020/5/8 14:22
 * @Version      1.0.0.0
 **/
@Component
public class ExampleDataSyncFactory implements DataSyncFactory {
    private DataSync dataSync;

    @Override
    public DataSync createDataSync() {
        if (dataSync == null) {
            dataSync = SpringBootBeanUtil.getBean(ExampleDataSync.class);
        }
        return dataSync;
    }
}
