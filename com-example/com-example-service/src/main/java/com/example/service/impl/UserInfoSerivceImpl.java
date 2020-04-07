package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.mapper.UserInfoMapper;
import com.example.domain.entity.UserInfoEntity;
import com.example.manager.UserInfoManager;
import com.example.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @PackagePath com.example.service.impl.UserInfoSerivceImpl
 * @Author YINZHIYU
 * @Date 2020-04-07 10:00:00
 * @Version 1.0.0.0
 **/
@Service
public class UserInfoSerivceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {

    @Resource
    private UserInfoManager userInfoManager;

    @Override
    public boolean saveUser(UserInfoEntity userInfoEntity) {
        return userInfoManager.saveUser(userInfoEntity) > 0;
    }

    @Override
    public boolean batchInsert(List<UserInfoEntity> list) {
        return this.baseMapper.batchInsert(list) > 0;
    }
}
