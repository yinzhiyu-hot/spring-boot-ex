package com.example.config;

import com.example.common.utils.SpringBootBeanUtil;
import com.example.service.job.cache.impl.JobsConfigCache;
import com.example.service.job.cache.impl.SysBaseConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Description 系统启动中加载各种全局配置
 * @PackagePath com.example.config.AppCacheConfig
 * @Author YINZHIYU
 * @Date 2020/5/22 11:46
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class AppCacheConfig {

    /**
     * 此处引用只为初始化 applicationContext
     */
    @Resource
    public SpringBootBeanUtil springBootBeanUtil;

    @Resource
    public JobsConfigCache jobsConfigCache;

    @Resource
    public SysBaseConfigCache sysBaseConfigCache;

    @PostConstruct
    public void init() {
        log.info("系统启动中");

        //初始化Job配置
        jobsConfigCache.init();

        //初始化基础配置
        sysBaseConfigCache.init();
    }

    @PreDestroy
    public void destroy() {
        log.info("系统运行结束");
    }
}
