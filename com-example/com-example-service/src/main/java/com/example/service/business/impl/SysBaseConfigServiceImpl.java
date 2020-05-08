package com.example.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.SysBaseConfigMapper;
import com.example.domain.entity.SysBaseConfig;
import com.example.service.business.SysBaseConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 基础信息配置业务类
 * @PackagePath com.example.service.business.impl.SysBaseConfigServiceImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:59
 * @Version 1.0.0.0
 **/
@Service
public class SysBaseConfigServiceImpl extends ServiceImpl<SysBaseConfigMapper, SysBaseConfig> implements SysBaseConfigService {

    @Override
    public int updateBatch(List<SysBaseConfig> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<SysBaseConfig> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(SysBaseConfig record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(SysBaseConfig record) {
        return baseMapper.insertOrUpdateSelective(record);
    }
}
