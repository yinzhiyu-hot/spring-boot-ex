package com.example.service.job.cache;

import com.example.common.utils.SpringBootBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Description 系统启动中加载jobs配置, 且初始化job
 * @PackagePath com.example.service.job.cache.AppCacheConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 14:14
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

        //初始化job配置
        jobsConfigCache.init();

        sysBaseConfigCache.init();
    }

    @PreDestroy
    public void destroy() {
        log.info("系统运行结束");
    }
}
