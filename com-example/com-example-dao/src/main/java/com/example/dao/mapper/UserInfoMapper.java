package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    /**
     * 查询大于该分数的学生
     * @Author Sans
     * @CreateTime 2019/6/9 14:28
     * @Param  page  分页参数
     * @Param  fraction  分数
     * @Return IPage<UserInfoEntity> 分页数据
     */
    IPage<UserInfoEntity> selectUserInfoByGtFraction(IPage<UserInfoEntity> page, Long fraction);

}
