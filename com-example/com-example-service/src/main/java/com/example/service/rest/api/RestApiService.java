package com.example.service.rest.api;

import com.example.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

/**
 * @Description TODO
 * @PackagePath com.example.service.rest.RestApiService
 * @Author YINZHIYU
 * @Date 2020-04-09 11:35:00
 * @Version 1.0.0.0
 **/
@Component
public interface RestApiService {

    @GET("/com-example/rest/getList")
    Call<List<UserInfoEntity>> userInfoList();
}
