package com.example.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.SyncTaskExceptionMapper;
import com.example.domain.entity.SyncTaskException;
import com.example.service.business.SyncTaskExceptionService;
import org.springframework.stereotype.Service;

/**
 * @Description 任务异常信息 服务实现类
 * @PackagePath com.example.service.business.impl.SyncTaskExceptionServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:58
 * @Version 1.0.0.0
 **/
@Service
public class SyncTaskExceptionServiceImpl extends ServiceImpl<SyncTaskExceptionMapper, SyncTaskException> implements SyncTaskExceptionService {

}
