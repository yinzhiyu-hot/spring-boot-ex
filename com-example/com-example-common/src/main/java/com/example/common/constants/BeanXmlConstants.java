package com.example.common.constants;

/**
 * @Description JavaBean, xml 互转常量类
 * @PackagePath com.example.common.constants.BeanXmlConstants
 * @Author YINZHIYU
 * @Date 2020/5/8 13:46
 * @Version 1.0.0.0
 **/
public interface BeanXmlConstants {
    //xml转javabean 时，xml中取节点的匹配规则
    String FIELD_NAME = "FIELD_NAME";//javabean 字段名称
    String FIELD_ANNOTATION_NAME = "FIELD_ANNOTATION_NAME";//javabean @XmlElement注解里配置的名称,配合@XmlElementAnno注解
}
