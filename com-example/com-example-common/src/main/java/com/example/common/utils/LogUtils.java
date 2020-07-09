package com.example.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

/**
 * @Description 日志工具类
 * @Remark
 * @PackagePath com.example.common.utils.LogUtils
 * @Author YINZHIYU
 * @Date 2020/7/9 15:42
 * @Version 1.0.0.0
 **/
@Slf4j
public class LogUtils {

    /*
     * @Description info 输出信息
     * @Remark
     * @Params ==>
     * @Param objects
     * @Return void
     * @Date 2020/6/19 13:25
     * @Auther YINZHIYU
     */
    public static void info(Object... objects) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String result = getPrintInfo(stack[2], objects);
        if (ObjectUtil.isNotEmpty(result)) {
            log.info(result);
        }
    }

    /*
     * @Description debug 输出信息
     * @Remark
     * @Params ==>
     * @Param objects
     * @Return void
     * @Date 2020/6/19 13:25
     * @Auther YINZHIYU
     */
    public static void debug(Object... objects) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String result = getPrintInfo(stack[2], objects);
        if (ObjectUtil.isNotEmpty(result)) {
            log.debug(result);
        }
    }

    /*
     * @Description error 输出信息
     * @Remark
     * @Params ==>
     * @Param objects
     * @Return void
     * @Date 2020/6/19 13:25
     * @Auther YINZHIYU
     */
    public static void error(Object... objects) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String result = getPrintInfo(stack[2], objects);
        if (ObjectUtil.isNotEmpty(result)) {
            log.error(result);
        }
    }

    /*
     * @Description 组装日志信息
     * @Remark
     * @Params ==>
     * @Param objects
     * @Return java.lang.String
     * @Date 2020/7/7 10:08
     * @Auther YINZHIYU
     */
    public static String getLogsInfo(Object... objects) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        return getPrintInfo(stack[2], objects);
    }

    /*
     * @Description 获取打印信息
     * @Remark
     * @Params ==>
     * @Param element
     * @Param objects
     * @Return java.lang.String
     * @Date 2020/6/19 13:25
     * @Auther YINZHIYU
     */
    private static String getPrintInfo(StackTraceElement element, Object... objects) {

        String recordDate = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MS_PATTERN);
        String fileName = element.getFileName();
        String className = element.getClassName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        LogsEntity logsEntity = new LogsEntity();
        logsEntity.setRecordDate(recordDate);
        logsEntity.setFileName(fileName);
        logsEntity.setClassName(className);
        logsEntity.setMethodName(methodName);
        logsEntity.setLineNumber(lineNumber);

        boolean hasException = false;
        //参数列表
        List<Object> paramObjs = Lists.newArrayList();
        //异常列表
        List<Exception> exObjs = Lists.newArrayList();

        try {
            //有参数就打印
            if (objects.length > 0) {
                for (Object obj : objects) {
                    if (obj instanceof Exception) {
                        //筛选出异常信息
                        exObjs.add((Exception) obj);
                    } else {
                        //筛选出参数
                        paramObjs.add(obj);
                    }
                }
                if (ObjectUtil.isNotEmpty(paramObjs)) {
                    logsEntity.setParamters(getParamterInfo(paramObjs));
                }
                if (ObjectUtil.isNotEmpty(exObjs)) {
                    //拼接异常
                    logsEntity.setExceptions(getExceptionInfo(exObjs));
                }
            }
        } catch (Exception e) {
            //拼接异常
            exObjs.add(e);
            logsEntity.setExceptions(getExceptionInfo(exObjs));
        }

        String json = JSONUtil.toJsonStr(logsEntity);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonFormatterStr = "";
        try {
            jsonFormatterStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(logsEntity);
        } catch (Exception e) {

        }
        StringBuilder builder = new StringBuilder();
        if (ObjectUtil.isNotEmpty(jsonFormatterStr)) {
            builder.append("\r\n");
            builder.append(jsonFormatterStr);
            builder.append("\r\n");
        }
        return builder.toString();
    }

    /*
     * @Description 获取参数
     * @Remark
     * @Params ==>
     * @Param params
     * @Return java.util.List<cn.wangoon.common.utils.LogUtils.ParamterEntity>
     * @Date 2020/6/19 13:24
     * @Auther YINZHIYU
     */
    private static List<ParamterEntity> getParamterInfo(List<Object> params) {
        List<ParamterEntity> paramterEntityList = Lists.newArrayList();

        if (ObjectUtil.isEmpty(params)) {
            return paramterEntityList;
        }

        for (Object obj : params) {
            paramterEntityList.add(getValue(obj));
        }

        return paramterEntityList;
    }

    /*
     * @Description 获取异常信息
     * @Remark
     * @Params ==>
     * @Param exs
     * @Return java.util.List<cn.wangoon.common.utils.LogUtils.ExceptionEntity>
     * @Date 2020/6/19 13:24
     * @Auther YINZHIYU
     */
    private static List<ExceptionEntity> getExceptionInfo(List<Exception> exs) {

        List<ExceptionEntity> exceptionEntityList = Lists.newArrayList();

        if (ObjectUtil.isEmpty(exs)) {
            return exceptionEntityList;
        }
        int count = 1;
        for (Exception ex : exs) {
            StringWriter sw = new StringWriter();
            //将详细异常用流输出
            PrintWriter pw = new PrintWriter(sw, true);
            ex.printStackTrace(pw);
            pw.flush();
            sw.flush();
            ExceptionEntity exceptionEntity = new ExceptionEntity();
            exceptionEntity.setMessage(ex.getMessage());
            exceptionEntity.setExceptionDetails(sw.toString());
            exceptionEntityList.add(exceptionEntity);
        }
        return exceptionEntityList;
    }

    /*
     * @Description 获取参数值,对象JSON化
     * @Remark
     * @Params ==>
     * @Param object
     * @Return cn.wangoon.common.utils.LogUtils.ParamterEntity
     * @Date 2020/6/19 13:24
     * @Auther YINZHIYU
     */
    private static ParamterEntity getValue(Object object) {
        ParamterEntity paramterEntity = new ParamterEntity();
        if (ObjectUtil.isNotEmpty(object)) {
            paramterEntity.setParamType(object.getClass().getTypeName());
            paramterEntity.setParamValue(object);
        } else {
            paramterEntity.setParamType("参数为空");
            paramterEntity.setParamValue(object);
        }
        return paramterEntity;
    }

    /**
     * @Description 日志实体
     * @Remark 未遵循驼峰，只为好看
     * @PackagePath cn.wangoon.common.utils.LogUtils
     * @Author YINZHIYU
     * @Date 2020/6/19 10:12
     * @Version 1.0.0.0
     **/
    @Data
    static class LogsEntity implements Serializable {
        String recordDate;
        String fileName;
        String className;
        String methodName;
        List<ParamterEntity> paramters;
        List<ExceptionEntity> exceptions;
        int lineNumber;
    }

    /**
     * @Description 参数实体
     * @Remark
     * @PackagePath cn.wangoon.common.utils.LogUtils
     * @Author YINZHIYU
     * @Date 2020/6/19 10:13
     * @Version 1.0.0.0
     **/
    @Data
    static class ParamterEntity implements Serializable {
        String paramType;
        Object paramValue;
    }

    /**
     * @Description 异常实体
     * @Remark
     * @PackagePath cn.wangoon.common.utils.LogUtils
     * @Author YINZHIYU
     * @Date 2020/6/19 13:24
     * @Version 1.0.0.0
     **/
    @Data
    static class ExceptionEntity implements Serializable {
        String message;
        String exceptionDetails;
    }
}