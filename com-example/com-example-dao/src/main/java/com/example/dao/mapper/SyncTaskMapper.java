package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dto.SyncTaskDto;
import com.example.domain.entity.SyncTask;
import com.example.domain.vo.SyncTaskChartVO;

import java.util.List;

/**
 * @Description 同步任务 Mapper 接口
 * @PackagePath com.example.dao.mapper.SyncTaskMapper
 * @Author YINZHIYU
 * @Date 2020/5/8 13:50
 * @Version 1.0.0.0
 **/
public interface SyncTaskMapper extends BaseMapper<SyncTask> {
    List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto);

    List<SyncTaskChartVO> getTaskChartsList(SyncTaskDto syncTaskDto);
}
