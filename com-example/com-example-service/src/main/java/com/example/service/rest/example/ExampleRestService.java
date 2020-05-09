package com.example.service.rest.example;

import cn.hutool.core.util.ObjectUtil;
import com.example.domain.entity.UserInfoEntity;
import com.example.service.rest.BaseRestService;
import com.example.service.rest.example.api.ExampleRestApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Description 示例接口
 * @PackagePath com.example.service.rest.example.ExampleRestService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:07
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class ExampleRestService extends BaseRestService {

    public List<UserInfoEntity> userInfoList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(restConfig.exampleUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ExampleRestApi service = retrofit.create(ExampleRestApi.class);
        Response<List<UserInfoEntity>> response = null;
        try {
            response = service.userInfoList().execute();

            if (ObjectUtil.isNotEmpty(response.errorBody())) {
                log.error(String.format("ExampleRestService ==> userInfoList ==> service.userInfoList().execute() ==> 调用失败：%s", response.errorBody().string()));
            }
        } catch (IOException e) {
            log.error(String.format("ExampleRestService -> userInfoList -> 异常：%s", e));
            return null;
        }
        return response.body();
    }
}
