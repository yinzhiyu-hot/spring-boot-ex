package com.example.manager;

import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;

/**
 * @Description 测试Manager
 * @PackagePath com.example.manager.UserInfoManager
 * @Author YINZHIYU
 * @Date 2020/5/8 13:57
 * @Version 1.0.0.0
 **/
@Component
public interface UserInfoManager {
    int saveUser(UserInfoEntity userInfoEntity);
}
