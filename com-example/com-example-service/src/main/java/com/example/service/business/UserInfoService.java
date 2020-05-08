package com.example.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 测试业务接口
 * @PackagePath com.example.service.business.UserInfoService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:01
 * @Version 1.0.0.0
 **/
@Component
public interface UserInfoService extends IService<UserInfoEntity> {

    boolean saveUser(UserInfoEntity userInfoEntity);

    boolean batchInsert(List<UserInfoEntity> list);

    /**
     * 查询大于该分数的学生
     *
     * @Author Sans
     * @CreateTime 2019/6/9 14:27
     * @Param page  分页参数
     * @Param fraction  分数
     * @Return IPage<UserInfoEntity> 分页数据
     */
    IPage<UserInfoEntity> selectUserInfoByGtFraction(IPage<UserInfoEntity> page, Long fraction);

}
