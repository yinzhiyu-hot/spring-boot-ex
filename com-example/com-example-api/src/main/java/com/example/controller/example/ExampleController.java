package com.example.controller.example;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.example.common.config.JobsConfig;
import com.example.common.constants.JobsConstants;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.SpringBootBeanUtil;
import com.example.domain.common.Result;
import com.example.domain.entity.UserInfoEntity;
import com.example.domain.vo.UserInfoVO;
import com.example.service.business.UserInfoService;
import com.example.service.job.cache.JobsConfigCache;
import io.swagger.annotations.Api;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description 小例子
 * @PackagePath com.example.controller.example.ExampleController
 * @Author YINZHIYU
 * @Date 2020/5/8 13:43
 * @Version 1.0.0.0
 **/
@Api(tags = "小例子自测-莫搞")
@RestController
@RequestMapping(value = "/rest")
public class ExampleController {

    @Resource
    RedisUtils redisUtils;

    @Resource
    private UserInfoService userInfoService;

    Map<String, JobScheduler> maps = new HashMap<>();

    @GetMapping(value = "/restEx/{id}")
    public String restEx(@PathVariable(name = "id") String id) {
        return String.format("%s,%s", "Hello", id);
    }

    /**
     * 根据ID获取用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:34
     * @Param userId  用户ID
     * @Return UserInfoEntity 用户实体
     */
    @GetMapping("/lockTest/{userId}")
    public String lockTest(@PathVariable(name = "userId") String userId) {
        String key = "myLock_" + userId;
        int timeout = 300 * 1000;//超时时间 5分钟
        long value = System.currentTimeMillis() + timeout;
        // 加锁
        if (!redisUtils.setLock(key, String.valueOf(value))) {
            throw new RuntimeException("对不起，redis被挤爆了，请休息片刻再重试。");
        }
        return key;
    }

    /**
     * 根据ID获取用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:34
     * @Param userId  用户ID
     * @Return UserInfoEntity 用户实体
     */
    @GetMapping("/getInfo/{userId}")
    public UserInfoEntity getInfo(@PathVariable(name = "userId") String userId) {
        return userInfoService.getById(userId);
    }

    /**
     * 查询全部信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:35
     * @Param userId  用户ID
     * @Return List<UserInfoEntity> 用户实体集合
     */
    @GetMapping("/getList")
    public List<UserInfoEntity> getList() {
        return userInfoService.list();
    }

    /**
     * 分页查询全部数据
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:37
     * @Return IPage<UserInfoEntity> 分页数据
     */
    @GetMapping("/getInfoListPage")
    public IPage<UserInfoEntity> getInfoListPage(UserInfoVO userInfoVO) {
        //需要在Config配置类中配置分页插件
        IPage<UserInfoEntity> page = new Page<>();
        page.setCurrent(userInfoVO.getPageNumber()); //当前页
        page.setSize(userInfoVO.getPageSize());    //每页条数
        page = userInfoService.page(page);
        return page;
    }

    /**
     * MP自定义SQL
     *
     * @Author Sans
     * @CreateTime 2019/6/9 14:37
     * @Return IPage<UserInfoEntity> 分页数据
     */
    @GetMapping("/getInfoListSQL")
    public IPage<UserInfoEntity> getInfoListSQL(UserInfoVO userInfoVO) {
        //查询大于60分以上的学生,并且分页
        IPage<UserInfoEntity> page = new Page<>();
        page.setCurrent(userInfoVO.getPageNumber()); //当前页
        page.setSize(userInfoVO.getPageSize());    //每页条数
        page = userInfoService.selectUserInfoByGtFraction(page, 60L);
        return page;
    }

    /**
     * 根据指定字段查询用户信息集合
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:39
     * @Return Collection<UserInfoEntity> 用户实体集合
     */
    @GetMapping("/getListMap")
    public Collection<UserInfoEntity> getListMap() {
        Map<String, Object> map = new HashMap<>();
        //kay是字段名 value是字段值
        map.put("age", 20);
        return userInfoService.listByMap(map);
    }

    /**
     * 新增用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:40
     */
    @GetMapping("/saveInfo")
    public void saveInfo() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setName("小龙");
        userInfoEntity.setSkill("JAVA");
        userInfoEntity.setAge(18);
        userInfoEntity.setFraction(59L);
        userInfoEntity.setEvaluate("该学生是一个在改BUG的码农");
        userInfoService.saveUser(userInfoEntity);
    }

    /**
     * 批量新增用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:42
     */
    @GetMapping("/saveInfoList")
    public void saveInfoList() {
        //创建对象
        UserInfoEntity sans = new UserInfoEntity();
        sans.setName("Sans");
        sans.setSkill("睡觉");
        sans.setAge(18);
        sans.setFraction(60L);
        sans.setEvaluate("Sans是一个爱睡觉,并且身材较矮骨骼巨大的骷髅小胖子");
        UserInfoEntity papyrus = new UserInfoEntity();
        papyrus.setName("papyrus");
        papyrus.setSkill("JAVA");
        papyrus.setAge(18);
        papyrus.setFraction(58L);
        papyrus.setEvaluate("Papyrus是一个讲话大声、个性张扬的骷髅，给人自信、有魅力的骷髅小瘦子");
        //批量保存
        List<UserInfoEntity> list = new ArrayList<>();
        list.add(sans);
        list.add(papyrus);
        userInfoService.batchInsert(list);//较下面耗时长
        userInfoService.saveBatch(list);//耗时短
    }

    /**
     * 更新用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:47
     */
    @GetMapping("/updateInfo")
    public void updateInfo() {
        //根据实体中的ID去更新,其他字段如果值为null则不会更新该字段,参考yml配置文件
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setId(1L);
        userInfoEntity.setAge(19);
        userInfoService.updateById(userInfoEntity);
    }

    /**
     * 新增或者更新用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:50
     */
    @GetMapping("/saveOrUpdateInfo")
    public void saveOrUpdate() {
        //传入的实体类userInfoEntity中ID为null就会新增(ID自增)
        //实体类ID值存在,如果数据库存在ID就会更新,如果不存在就会新增
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setId(1L);
        userInfoEntity.setAge(20);
        userInfoService.saveOrUpdate(userInfoEntity);
    }

    /**
     * 根据ID删除用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:52
     */
    @GetMapping("/deleteInfo")
    public void deleteInfo(String userId) {
        userInfoService.removeById(userId);
    }

    /**
     * 根据ID批量删除用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:55
     */
    @GetMapping("/deleteInfoList")
    public void deleteInfoList() {
        List<String> userIdlist = new ArrayList<>();
        userIdlist.add("12");
        userIdlist.add("13");
        userInfoService.removeByIds(userIdlist);
    }

    /**
     * 根据指定字段删除用户信息
     *
     * @Author Sans
     * @CreateTime 2019/6/8 16:57
     */
    @GetMapping("/deleteInfoMap")
    public void deleteInfoMap() {
        //kay是字段名 value是字段值
        Map<String, Object> map = new HashMap<>();
        map.put("skill", "删除");
        map.put("fraction", 10L);
        userInfoService.removeByMap(map);
    }

    @PostMapping(value = "/messagePost")
    public Result messagePost(@RequestParam RequestEntity requestEntity) {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");
        return result;
    }

    /**
     * 数据库新，并且加入缓存
     *
     * @return
     */
    @GetMapping(value = "/add")
    public Result add() {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");

        JobScheduler jobScheduler = createJobScheduler();
        jobScheduler.init();
        jobScheduler.getSchedulerFacade().registerStartUpInfo(false);//是否启动
        maps.put(JobsConstants.HEART_JOB_CLASS_BEAN_NAME, jobScheduler);
        return result;
    }

    /**
     * 数据库删除，并且移除缓存
     *
     * @return
     */
    @GetMapping(value = "/remove")
    public Result remove() {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");
        maps.remove(JobsConstants.HEART_JOB_CLASS_BEAN_NAME);
        return result;
    }

    @GetMapping(value = "/start")
    public Result start() {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");

        JobsConfigCache.jobSchedulerMap.get(JobsConstants.HEART_JOB_CLASS_BEAN_NAME).getSchedulerFacade().registerStartUpInfo(true);//是否启动

        return result;
    }

    @GetMapping(value = "/update")
    public Result update() {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");

        SimpleJob syncTaskJob = (SimpleJob) SpringBootBeanUtil.getBean(jobClassBeanName);

        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(syncTaskJob.getClass(), "0/1 * * * * ?", 2, shardingItemParameters);

        JobsConfigCache.jobSchedulerMap.get(JobsConstants.HEART_JOB_CLASS_BEAN_NAME).getSchedulerFacade().updateJobConfiguration(liteJobConfiguration);

        return result;
    }

    @GetMapping(value = "/stop")
    public Result stop() {
        Result<String> result = new Result<String>();
        result.setCode(0);
        result.setDatas("返回了");

        JobsConfigCache.jobSchedulerMap.get(JobsConstants.HEART_JOB_CLASS_BEAN_NAME).getSchedulerFacade().registerStartUpInfo(false);

        return result;
    }

    String cron = "0/5 * * * * ?";

    int shardingTotalCount = 1;

    String shardingItemParameters;

    String jobClassBeanName = JobsConstants.HEART_JOB_CLASS_BEAN_NAME;

    @Resource
    JobsConfig jobsConfig;

    public JobScheduler createJobScheduler() {

        JobScheduler jobScheduler = null;
        try {
            SimpleJob syncTaskJob = (SimpleJob) SpringBootBeanUtil.getBean(jobClassBeanName);

            System.out.println(String.format("getName=%s  getCanonicalName=%s", syncTaskJob.getClass().getName(), syncTaskJob.getClass().getCanonicalName()));

            jobScheduler = jobsConfig.addSimpleJobScheduler(syncTaskJob, cron, shardingTotalCount, shardingItemParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobScheduler;
    }

    /**
     * @Description 任务配置类
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {


        return LiteJobConfiguration
                .newBuilder(
                        new SimpleJobConfiguration(
                                JobCoreConfiguration.newBuilder(
                                        jobClass.getName(), cron, shardingTotalCount)
                                        .shardingItemParameters(shardingItemParameters)
                                        .build()
                                , jobClass.getCanonicalName()
                        )
                )
                .overwrite(true)
                .build();

    }
}
