package com.example.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.entity.SysBaseConfig;

import java.util.List;

/**
 * @Description 基础信息配置业务接口
 * @PackagePath com.example.service.business.SysBaseConfigService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:00
 * @Version 1.0.0.0
 **/
public interface SysBaseConfigService extends IService<SysBaseConfig> {


    int updateBatch(List<SysBaseConfig> list);

    int batchInsert(List<SysBaseConfig> list);

    int insertOrUpdate(SysBaseConfig record);

    int insertOrUpdateSelective(SysBaseConfig record);

}
