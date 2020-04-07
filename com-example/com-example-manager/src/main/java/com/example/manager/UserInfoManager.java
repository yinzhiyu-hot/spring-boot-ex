package com.example.manager;

import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @PackagePath com.example.manager.UserInfoManager
 * @Author YINZHIYU
 * @Date 2020-04-07 10:01:00
 * @Version 1.0.0.0
 **/
@Component
public interface UserInfoManager {
    public int saveUser(UserInfoEntity userInfoEntity);
}
