package com.example.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 响应结果类
 * @PackagePath com.example.domain.common.Result
 * @Author YINZHIYU
 * @Date 2020/5/8 13:53
 * @Version 1.0.0.0
 **/
@ApiModel(description = "响应结果")
@Data
public class Result<T> implements Serializable {

    @ApiModelProperty(value = "成功与否")
    private Boolean success;
    @ApiModelProperty(value = "返回码(成功标记=0，失败标记=1)")
    private Integer code;//返回标记：成功标记=0，失败标记=1
    @ApiModelProperty(value = "错误信息")
    private String error;
    @ApiModelProperty(value = "业务信息")
    private String msg;
    @ApiModelProperty(value = "响应内容")
    private T datas;

    public Result() {
    }

    public Result(T datas) {
        this.datas = datas;
    }

    public static <T> Result<T> ok(T datas) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.success = true;
        result.msg = "处理成功";
        result.datas = datas;
        return result;
    }

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.code = 0;
        result.success = true;
        result.msg = "处理成功";
        return result;
    }

    public static <T> Result<T> fail(String fail) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.success = false;
        result.msg = "处理失败";
        result.error = fail;
        result.datas = null;
        return result;
    }

    public static <T> Result<T> exception(String error) {
        Result<T> result = new Result<>();
        result.code = 99;
        result.success = false;
        result.msg = "处理异常";
        result.error = error;
        result.datas = null;
        return result;
    }

}
