package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.SyncTaskMapper;
import com.example.domain.dto.SyncTaskDto;
import com.example.domain.entity.SyncTask;
import com.example.manager.SyncTaskManager;
import com.example.service.business.SyncTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 同步任务业务类
 * @PackagePath com.example.service.business.impl.SyncTaskServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:58
 * @Version 1.0.0.0
 **/
@Service
public class SyncTaskServiceImpl extends ServiceImpl<SyncTaskMapper, SyncTask> implements SyncTaskService {

    @Resource
    private SyncTaskManager syncTaskManager;

    @Override
    public boolean insertSyncTask(SyncTask syncTask) {
        return syncTaskManager.insertSyncTask(syncTask);
    }

    @Override
    public boolean batchInsertSyncTask(List<SyncTask> syncTaskList) {
        return syncTaskManager.batchInsertSyncTask(syncTaskList);
    }

    @Transactional
    public List<SyncTask> selectList(Wrapper<SyncTask> wrapper) {
        return super.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto) {
        return this.baseMapper.getProcessTaskList(syncTaskDto);
    }
}
