package com.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description Rest 配置
 * @PackagePath com.example.common.config.RestConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:45
 * @Version 1.0.0.0
 **/
@Component
public class RestConfig {
    @Value("${rest.example.domain}")
    public String exampleUrl;
}
