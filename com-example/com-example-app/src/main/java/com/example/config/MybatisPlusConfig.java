package com.example.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description MybatisPlus配置类
 * @PackagePath com.example.dao.config.MybatisPlusConfig
 * @Author YINZHIYU
 * @Date 2020-04-07 09:37:00
 * @Version 1.0.0.0
 **/
@Configuration
@MapperScan(basePackages = {"com.example.dao.mapper"}) //扫描Mapper接口
public class MybatisPlusConfig {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
