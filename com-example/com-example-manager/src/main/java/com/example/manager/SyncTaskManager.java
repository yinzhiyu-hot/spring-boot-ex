package com.example.manager;

import com.example.domain.entity.SyncTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 同步任务 Mapper 接口
 * @PackagePath com.example.manager.SyncTaskManager
 * @Author YINZHIYU
 * @Date 2020/5/8 13:57
 * @Version 1.0.0.0
 **/
@Component
public interface SyncTaskManager {

    boolean insertSyncTask(SyncTask syncTask);

    boolean batchInsertSyncTask(List<SyncTask> syncTaskList);
}
