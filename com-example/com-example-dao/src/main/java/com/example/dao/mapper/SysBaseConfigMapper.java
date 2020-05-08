package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.entity.SysBaseConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 基础信息配置 Mapper 接口
 * @PackagePath com.example.dao.mapper.SysBaseConfigMapper
 * @Author YINZHIYU
 * @Date 2020/5/8 13:51
 * @Version 1.0.0.0
 **/
public interface SysBaseConfigMapper extends BaseMapper<SysBaseConfig> {

    int updateBatch(List<SysBaseConfig> list);

    int batchInsert(@Param("list") List<SysBaseConfig> list);

    int insertOrUpdate(SysBaseConfig record);

    int insertOrUpdateSelective(SysBaseConfig record);
}