package com.example.manager.impl;

import com.example.dao.mapper.SyncTaskDataMapper;
import com.example.dao.mapper.SyncTaskMapper;
import com.example.domain.entity.SyncTask;
import com.example.manager.SyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 异步任务Manager实现
 * @PackagePath com.example.manager.impl.SyncTaskManagerImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:57
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class SyncTaskManagerImpl implements SyncTaskManager {
    @Resource
    private SyncTaskMapper syncTaskMapper;

    @Resource
    private SyncTaskDataMapper syncTaskDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSyncTask(SyncTask syncTask) {
        boolean result;
        try {
            //插入主档
            syncTask.setId(null);
            result = syncTaskMapper.insert(syncTask) > 0;

            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl -> insertSyncTask -> syncTaskMapper.insert(syncTask) return false");
            }
            //插入明细
            syncTask.getSyncTaskDataList().stream().filter(bean -> {
                bean.setTaskId(syncTask.getId());
                return true;
            }).collect(Collectors.toList());

            result = syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) > 0;

            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl -> insertSyncTask -> syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) return false");
            }

        } catch (Exception ex) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            log.error(String.format("SyncTaskManagerImpl -> insertSyncTask - > 异常：%s", ex));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertSyncTask(List<SyncTask> syncTaskList) {
        boolean result = false;
        try {
            for (SyncTask syncTask : syncTaskList) {
                //插入主档
                syncTask.setId(null);
                result = syncTaskMapper.insert(syncTask) > 0;

                if (!result) {
                    throw new RuntimeException("SyncTaskManagerImpl -> insertSyncTask -> syncTaskMapper.insert(syncTask) return false");
                }
                //插入明细
                syncTask.getSyncTaskDataList().stream().filter(bean -> {
                    bean.setTaskId(syncTask.getId());
                    return true;
                }).collect(Collectors.toList());

                result = syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) > 0;

                if (!result) {
                    throw new RuntimeException("SyncTaskManagerImpl -> insertSyncTask -> syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) return false");
                }
            }
        } catch (Exception ex) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            log.error(String.format("SyncTaskManagerImpl -> batchInsertSyncTask - > 异常：%s", ex));
        }
        return result;
    }
}
