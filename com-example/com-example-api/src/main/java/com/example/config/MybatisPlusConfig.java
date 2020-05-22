package com.example.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * @Description MybatisPlus配置类
 * @PackagePath com.example.config.MybatisPlusConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:42
 * @Version 1.0.0.0
 **/
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.example.dao.mapper"}) //扫描Mapper接口
public class MybatisPlusConfig {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @Profile({"dev", "test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        Properties properties = new Properties();
        properties.setProperty("maxTime", "2000");//SQL 最大执行时长（单位/毫秒）[超过此时长mp将报错,便于开发，测试时发现慢sql]
        properties.setProperty("format", "false");//SQL 是否格式化
        performanceInterceptor.setProperties(properties);
        return performanceInterceptor;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
