package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.example.common.utils.LogUtils;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 将zk  cron  job 建立关系
 * @PackagePath com.example.common.config.JobsConfig
 * @Author YINZHIYU
 * @Date 2020/5/8 13:45
 * @Version 1.0.0.0
 **/
@Configuration
public class JobsConfig {

    @Resource
    public ZookeeperRegistryCenter regCenter;

    public JobsConfig() {
    }
 
    /*
     * @Description job zk cron 建立关系
     * @Remark
     * @Params ==>
     * @Param simpleJob
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.api.JobScheduler
     * @Date 2020/9/17 9:51
     * @Auther YINZHIYU
     */
    public JobScheduler addSimpleJobScheduler(final SimpleJob simpleJob, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return new SpringJobScheduler(simpleJob, regCenter, getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
    }

    /*
     * @Description cron
     * @Remark
     * @Params ==>
     * @Param jobClass
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.config.LiteJobConfiguration
     * @Date 2020/9/17 9:51
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
     * @Description 获取某个节点下的所有子节点
     * @Remark
     * @Params ==>
     * @Param nodePath
     * @Return java.util.List<java.lang.String>
     * @Date 2020/9/16 16:09
     * @Auther YINZHIYU
     */
    public List<String> getChildNodes(String nodePath) {
        List<String> childrenList = Lists.newArrayList();
        try {
            CuratorFramework client = regCenter.getClient();
            if (ObjectUtil.isEmpty(client)) {
                return childrenList;
            }
            Stat stat = new Stat();
            GetChildrenBuilder childrenBuilder = client.getChildren();
            childrenList.addAll(childrenBuilder.storingStatIn(stat).forPath(nodePath));
        } catch (Exception e) {
            LogUtils.error(String.format("获取Zookeeper下Oms系统 节点%s下所有节点 ==> 异常", nodePath), e);
        }

        return childrenList.stream().map(item -> item = String.format("/%s", item)).collect(Collectors.toList());
    }

    /*
     * @Description 删除某个节点及子节点
     * @Remark
     * @Params ==>
     * @Param nodePath
     * @Return void
     * @Date 2020/9/16 17:04
     * @Auther YINZHIYU
     */
    public void deleteNode(String nodePath) {
        try {
            CuratorFramework client = regCenter.getClient();
            if (ObjectUtil.isEmpty(client)) {
                return;
            }
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(nodePath);
            client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(nodePath);
        } catch (Exception e) {
            LogUtils.error(String.format("删除Zookeeper下Oms系统 节点%s以及子节点 ==> 异常", nodePath), e);
        }
    }
}