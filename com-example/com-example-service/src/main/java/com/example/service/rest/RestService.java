package com.example.service.rest;

import cn.hutool.json.JSONConverter;
import com.example.domain.entity.UserInfoEntity;
import com.example.service.rest.api.RestApiService;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Description TODO
 * @PackagePath com.example.service.rest.RestService
 * @Author YINZHIYU
 * @Date 2020-04-09 11:39:00
 * @Version 1.0.0.0
 **/
@Component
public class RestService {

    public List<UserInfoEntity> getUserInfoList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApiService service = retrofit.create(RestApiService.class);
        Response<List<UserInfoEntity>> response = null;
        try {
            response = service.userInfoList().execute();
        } catch (IOException e) {
            return null;
        }
        return response.body();
    }
}
