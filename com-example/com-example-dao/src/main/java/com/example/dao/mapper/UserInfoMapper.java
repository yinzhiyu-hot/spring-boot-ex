package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description TODO
 * @PackagePath com.example.dao.mapper.UserInfoMapper
 * @Author YINZHIYU
 * @Date 2020-04-07 09:58:00
 * @Version 1.0.0.0
 **/
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {
    int batchInsert(@Param("list") List<UserInfoEntity> list);
}
