package com.example.service.business;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dto.SyncTaskDto;
import com.example.domain.entity.SyncTask;
import com.example.domain.vo.SyncTaskChartVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 同步任务业务接口
 * @PackagePath com.example.service.business.SyncTaskService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:00
 * @Version 1.0.0.0
 **/
@Component
public interface SyncTaskService extends IService<SyncTask> {

    boolean insertSyncTask(SyncTask syncTask);

    boolean batchInsertSyncTask(List<SyncTask> syncTaskList);

    List<SyncTask> selectList(Wrapper<SyncTask> wrapper);

    List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto);

    List<SyncTaskChartVO> getTaskChartsList(SyncTaskDto syncTaskDto);
}
