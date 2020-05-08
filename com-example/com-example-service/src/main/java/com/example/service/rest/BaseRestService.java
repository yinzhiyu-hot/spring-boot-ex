package com.example.service.rest;

import com.example.common.config.RestConfig;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description 基础Rest
 * @PackagePath com.example.service.rest.BaseRestService
 * @Author YINZHIYU
 * @Date 2020/5/8 14:07
 * @Version 1.0.0.0
 **/
@Component
public class BaseRestService {

    @Resource
    public RestConfig restConfig;

    protected static final OkHttpClient client;

    static {
        client = new OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(30, TimeUnit.SECONDS).build();
    }
}
