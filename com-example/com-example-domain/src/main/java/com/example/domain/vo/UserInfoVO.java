package com.example.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 测试实体
 * @PackagePath com.example.domain.vo.UserInfoVO
 * @Author YINZHIYU
 * @Date 2020/5/8 13:56
 * @Version 1.0.0.0
 **/
@Data
public class UserInfoVO extends BasePageVO implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 技能
     */
    private String skill;

    /**
     * 评价
     */
    private String evaluate;

    /**
     * 分数
     */
    private Long fraction;

}

