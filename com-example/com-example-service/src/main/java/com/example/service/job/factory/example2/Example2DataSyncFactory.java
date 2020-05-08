package com.example.service.job.factory.example2;

import com.example.common.utils.SpringBootBeanUtil;
import com.example.service.job.factory.DataSync;
import com.example.service.job.factory.DataSyncFactory;
import com.example.service.job.factory.example.ExampleDataSync;
import org.springframework.stereotype.Component;

/**
 * @Description  TODO
 * @PackagePath com.example.service.job.factory.example2.Example2DataSyncFactory
 * @Author       YINZHIYU
 * @Date         2020/5/8 14:34
 * @Version      1.0.0.0
 **/
@Component
public class Example2DataSyncFactory implements DataSyncFactory {
    private DataSync dataSync;

    @Override
    public DataSync createDataSync() {
        if (dataSync == null) {
            dataSync = SpringBootBeanUtil.getBean(ExampleDataSync.class);
        }
        return dataSync;
    }
}
