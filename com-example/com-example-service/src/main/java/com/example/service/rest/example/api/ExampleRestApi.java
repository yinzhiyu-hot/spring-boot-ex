package com.example.service.rest.example.api;

import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/**
 * @Description 示例API接口调用定义
 * @PackagePath com.example.service.rest.example.api.ExampleRestApi
 * @Author YINZHIYU
 * @Date 2020/5/8 14:07
 * @Version 1.0.0.0
 **/
@Component
public interface ExampleRestApi {

    @GET("/com-example/rest/getList")
    Call<List<UserInfoEntity>> userInfoList();
}
