package com.example.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;

import java.util.Map;

/**
 * @Description 网络请求工具
 * @PackagePath com.example.common.utils.HttpUtils
 * @Author YINZHIYU
 * @Date 2020/5/8 13:48
 * @Version 1.0.0.0
 **/
public class HttpUtils {

    /**
     * 请求post接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendPost(String url, String resultParams, Map<String, String> heads) {
        HttpRequest request = HttpRequest.post(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }


    /**
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendPut(String url, String resultParams, Map<String, String> heads) {
        HttpRequest request = HttpRequest.put(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }

    /**
     * 请求get接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendGet(String url, String resultParams, Map<String, String> heads) {
        HttpRequest request = HttpRequest.get(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }

    /**
     * 请求get接口
     *
     * @param url
     * @param params
     * @param heads
     * @return
     */
    public static String sendGet(String url, Map<String, Object> params, Map<String, String> heads) {
        HttpRequest request = HttpRequest.get(url);
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        if (ObjectUtil.isNotEmpty(params)) {
            request.form(params);
        }
        return request.execute().body();
    }

    /**
     * 请求post接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendKeyValuePost(String url, Map<String, Object> resultParams, Map<String, String> heads) {
        HttpRequest request = HttpRequest.post(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            for (Map.Entry<String, Object> entry : resultParams.entrySet()) {
                request.form(entry.getKey(), entry.getValue());
            }
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }
}
