package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.entity.SyncTaskData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 同步任务数据 Mapper 接口
 * @PackagePath com.example.dao.mapper.SyncTaskDataMapper
 * @Author YINZHIYU
 * @Date 2020/5/8 13:50
 * @Version 1.0.0.0
 **/
public interface SyncTaskDataMapper extends BaseMapper<SyncTaskData> {
    int batchInsert(@Param("list") List<SyncTaskData> list);
}
