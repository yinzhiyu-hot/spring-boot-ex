package com.example.domain.common;

import com.example.common.utils.StringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 分配参数解析
 * @PackagePath com.example.domain.common.ShardingItemParameters
 * @Author YINZHIYU
 * @Date 2020/5/8 13:53
 * @Version 1.0.0.0
 **/
@Data
public class ShardingItemParameters {
    public Map<String, String> map = new HashMap<>();

    public ShardingItemParameters(String shardingItemParameters) {
        if (StringUtils.notBlank(shardingItemParameters)) {
            String[] kvs = shardingItemParameters.split("#");
            for (String kvStr : kvs) {
                String[] kv = kvStr.split(":");
                map.put(kv[0], kv[1]);
            }
        }
    }
}
