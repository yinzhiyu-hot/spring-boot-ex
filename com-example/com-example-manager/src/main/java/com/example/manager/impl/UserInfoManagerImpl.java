package com.example.manager.impl;

import com.example.dao.mapper.UserInfoMapper;
import com.example.domain.entity.UserInfoEntity;
import com.example.manager.UserInfoManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * @Description 测试Manager 实现
 * @PackagePath com.example.manager.impl.UserInfoManagerImpl
 * @Author YINZHIYU
 * @Date 2020/5/8 13:57
 * @Version 1.0.0.0
 **/
@Component
public class UserInfoManagerImpl implements UserInfoManager {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveUser(UserInfoEntity userInfoEntity) {
        int count = 0;
        try {
            count = userInfoMapper.insert(userInfoEntity);
            userInfoMapper.deleteById(userInfoEntity.getId() - 1);
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
        }
        return count;
    }
}
