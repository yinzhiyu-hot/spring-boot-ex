package com.example.service.business.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.SyncTaskDataMapper;
import com.example.domain.entity.SyncTaskData;
import com.example.service.business.SyncTaskDataService;
import org.springframework.stereotype.Service;

/**
 * @Description 同步任务数据
 * @PackagePath com.example.service.business.impl.SyncTaskDataServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:58
 * @Version 1.0.0.0
 **/
@Service
public class SyncTaskDataServiceImpl extends ServiceImpl<SyncTaskDataMapper, SyncTaskData> implements SyncTaskDataService {

}
