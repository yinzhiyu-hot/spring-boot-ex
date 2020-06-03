package com.example.service.job.cache;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.enums.DelFlagEnum;
import com.example.domain.entity.SysBaseConfig;
import com.example.service.business.SysBaseConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 基础配置缓存
 * @PackagePath com.example.service.job.cache.SysBaseConfigCache
 * @Author YINZHIYU
 * @Date 2020/5/22 11:48
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class SysBaseConfigCache implements BaseCache {

    /**
     * sys_base_config 配置map
     * KEY -> biz_type  Value Map for bizType
     */
    public final static Map<String, Map<String, List<SysBaseConfig>>> sysBaseConfigMap = new HashMap<>();

    @Resource
    private SysBaseConfigService sysBaseConfigService;

    @Override
    public void init() {
        log.info("加载sysBaseConfigMap");
        QueryWrapper<SysBaseConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysBaseConfig.COL_DEL_FLAG, DelFlagEnum.NO.getFlag());
        List<SysBaseConfig> sysBaseConfigs = sysBaseConfigService.list(queryWrapper);
        convertMap(sysBaseConfigs);
    }

    /*
     * @Description 转换map
     * @Params ==>
     * @Param sysBaseConfigs
     * @Return void
     * @Date 2020/4/26 11:01
     * @Auther YINZHIYU
     */
    private void convertMap(List<SysBaseConfig> sysBaseConfigs) {
        if (ObjectUtil.isNotEmpty(sysBaseConfigs)) {

            sysBaseConfigMap.clear();

            //biz_type 分组
            Map<String, List<SysBaseConfig>> listBizTypeMap = sysBaseConfigs.stream().collect(Collectors.groupingBy(SysBaseConfig::getBizType));

            for (Map.Entry entry : listBizTypeMap.entrySet()) {
                //biz_key分组
                Map<String, List<SysBaseConfig>> listBizKeyMap = listBizTypeMap.get(entry.getKey().toString()).stream().collect(Collectors.groupingBy(SysBaseConfig::getBizKey));
                sysBaseConfigMap.put(entry.getKey().toString(), listBizKeyMap);
            }
        }
    }
}
