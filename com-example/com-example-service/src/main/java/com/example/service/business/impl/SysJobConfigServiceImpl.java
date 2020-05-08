package com.example.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.SysJobConfigMapper;
import com.example.domain.entity.SysJobConfig;
import com.example.service.business.SysJobConfigService;
import org.springframework.stereotype.Service;

/**
 * @Description JOB调度配置业务类
 * @PackagePath com.example.service.business.impl.SysJobConfigServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:59
 * @Version 1.0.0.0
 **/
@Service
public class SysJobConfigServiceImpl extends ServiceImpl<SysJobConfigMapper, SysJobConfig> implements SysJobConfigService {
}
