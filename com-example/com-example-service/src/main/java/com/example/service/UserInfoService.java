package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description TODO
 * @PackagePath com.example.service.UserInfoService
 * @Author YINZHIYU
 * @Date 2020-04-07 10:00:00
 * @Version 1.0.0.0
 **/
@Component
public interface UserInfoService extends IService<UserInfoEntity> {

    public boolean saveUser(UserInfoEntity userInfoEntity);

    public boolean batchInsert(List<UserInfoEntity> list);
}
