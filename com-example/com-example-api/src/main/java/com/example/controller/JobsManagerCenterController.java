package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.JobStatusEnum;
import com.example.common.utils.NetUtils;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.common.Result;
import com.example.domain.entity.SysJobConfig;
import com.example.domain.vo.BasePageVO;
import com.example.service.business.SysJobConfigService;
import com.example.service.job.cache.JobsConfigCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description Job 管理中心
 * @PackagePath com.example.controller.JobsManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/5/8 13:44
 * @Version 1.0.0.0
 **/
@Api(tags = "Job 管理中心")
@Slf4j
@Controller
@RequestMapping(value = "/jobs")
public class JobsManagerCenterController {
    @Resource
    Environment environment;

    @Resource
    RedisUtils redisUtils;

    @Resource
    private SysJobConfigService sysJobConfigService;

    @ApiOperation(value = "Job配置页面", notes = "Job配置页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages() {
        return "jobs_manager";
    }

    @ApiOperation(value = "Job配置信息分页", notes = "Job配置信息分页", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {
        Page<SysJobConfig> page = new Page<SysJobConfig>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SysJobConfig> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(sysJobConfig.getJobStatus())) {
            queryWrapper.eq(SysJobConfig.COL_JOB_STATUS, sysJobConfig.getJobStatus());
        }

        IPage<SysJobConfig> pages = sysJobConfigService.getBaseMapper().selectPage(page, queryWrapper);
        //bootstrap-table要求服务器返回的json须包含：total，rows
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        return map;
    }

    /*
     * @Description 启动
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "启动", notes = "启动", httpMethod = "POST")
    @PostMapping(value = "/start")
    @ResponseBody
    public Result start(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {

        try {
            //获取数据库调度配置信息
            SysJobConfig sysJobConfigLocal = sysJobConfigService.getById(sysJobConfig.getId());

            //启动调度
            JobsConfigCache.jobSchedulerMap.get(sysJobConfigLocal.getJobClassBeanName()).getSchedulerFacade().registerStartUpInfo(true);//是否启动

            //更新数据库状态
            sysJobConfigLocal.setJobStatus(JobStatusEnum.START.getStatus());
            sysJobConfigLocal.setUpdateTime(new Date());
            sysJobConfigService.updateById(sysJobConfigLocal);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);

            try {
                //获取Redis JOB 调度配置
                Map<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
            } catch (Exception e) {
                log.error(String.format("JobsManagerCenterController ==> start ==> 操作Redis ==> 异常：%s", e));
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }

    /*
     * @Description JOB 停止
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ResponseBody
    @ApiOperation(value = "停止", notes = "停止", httpMethod = "POST")
    @PostMapping(value = "/stop")
    public Result stop(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {
        try {
            //获取数据库调度配置信息
            SysJobConfig sysJobConfigLocal = sysJobConfigService.getById(sysJobConfig.getId());

            //启动调度
            JobsConfigCache.jobSchedulerMap.get(sysJobConfigLocal.getJobClassBeanName()).getSchedulerFacade().registerStartUpInfo(false);//是否启动

            //更新数据库状态
            sysJobConfigLocal.setJobStatus(JobStatusEnum.STOP.getStatus());
            sysJobConfigLocal.setUpdateTime(new Date());
            sysJobConfigService.updateById(sysJobConfigLocal);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);

            try {
                //获取Redis JOB 调度配置
                Map<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
            } catch (Exception e) {
                log.error(String.format("JobsManagerCenterController ==> stop ==> 操作Redis ==> 异常：%s", e));
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }

    /*
     * @Description JOB 更新
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "更新", notes = "更新", httpMethod = "POST")
    @PostMapping(value = "/update")
    @ResponseBody
    public Result update(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {

        try {
            sysJobConfig.setUpdateFlag(true);
            sysJobConfig.setUpdateIpPort(getLocalIpPort());

            //更新调度配置信息
            SimpleJob syncTaskJob = (SimpleJob) SpringBootBeanUtil.getBean(sysJobConfig.getJobClassBeanName());
            LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(syncTaskJob.getClass(), sysJobConfig.getCronExpression(), sysJobConfig.getShardingTotalCount(), sysJobConfig.getShardingItemParams());
            JobsConfigCache.jobSchedulerMap.get(sysJobConfig.getJobClassBeanName()).getSchedulerFacade().updateJobConfiguration(liteJobConfiguration);

            //更新数据库状态
            sysJobConfig.setUpdateTime(new Date());
            sysJobConfigService.updateById(sysJobConfig);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);

            try {
                //获取Redis JOB 调度配置
                Map<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
            } catch (Exception e) {
                log.error(String.format("JobsManagerCenterController ==> update ==> 操作Redis ==> 异常：%s", e));
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }

    /*
     * @Description 任务配置类
     * @Params ==>
     * @Param jobClass
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.config.LiteJobConfiguration
     * @Date 2020/4/27 18:04
     * @Auther YINZHIYU
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(
                        JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(), jobClass.getCanonicalName()
                )
        ).overwrite(true).build();
    }

    /*
     * @Description 获取Ip+端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:13
     * @Auther YINZHIYU
     */
    private String getLocalIpPort() {
        return String.format("%s:%s", NetUtils.getLocalIP(), getLocalPort());
    }

    /*
     * @Description 获取应用端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:11
     * @Auther YINZHIYU
     */
    private String getLocalPort() {
        return environment.getProperty("local.server.port");
    }
}
