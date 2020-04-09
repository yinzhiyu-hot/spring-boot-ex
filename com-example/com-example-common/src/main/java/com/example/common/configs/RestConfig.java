package com.example.common.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @PackagePath com.example.common.config.RestConfig
 * @Author YINZHIYU
 * @Date 2020-04-09 12:32:00
 * @Version 1.0.0.0
 **/
@Component
public class RestConfig {
    @Value("${rest.domain}")
    public String wmsUrl;
}
