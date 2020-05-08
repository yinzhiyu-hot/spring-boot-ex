package com.example.common.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description zookeeper 注册
 * @PackagePath com.example.common.config.ZookeeperRegistryCenterConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:46
 * @Version 1.0.0.0
 **/
@Configuration
@ConditionalOnExpression("'${regCenter.serverList}'.length() > 0")
public class ZookeeperRegistryCenterConfig {

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(@Value("${regCenter.serverList}") final String serverList, @Value("${regCenter.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }

}