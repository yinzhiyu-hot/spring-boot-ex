package com.example.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description SpringBoot bean 工具类
 * @PackagePath com.example.common.utils.SpringBootBeanUtil
 * @Author YINZHIYU
 * @Date 2020/5/8 13:49
 * @Version 1.0.0.0
 **/
@Component
public class SpringBootBeanUtil implements ApplicationContextAware {

    /**
     * Spring 上下文
     */
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBootBeanUtil.applicationContext == null) {
            SpringBootBeanUtil.applicationContext = applicationContext;
        }
    }

    /*
     * @Description 根据bean名称获取bean
     * @Params ==>
     * @Param name
     * @Return java.lang.Object
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /*
     * @Description 根据类获取bean
     * @Params ==>
     * @Param clazz
     * @Return T
     * @Date 2020/4/21 10:03
     * @Auther YINZHIYU
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /*
     * @Description 根据类和bean名称获取bean [存在多次注入同一个bean，但beanName不同]
     * @Params ==>
     * @Param clazz
     * @Param beanName
     * @Return T
     * @Date 2020/4/21 10:03
     * @Auther YINZHIYU
     */
    public static <T> T getBean(Class<T> clazz, String beanName) {
        return applicationContext.getBean(beanName, clazz);
    }

    /*
     * @Description 动态注册bean
     * @Params ==>
     * @Param clazz      类
     * @Param beanName   id
     * @Return T
     * @Date 2020/4/21 10:03
     * @Auther YINZHIYU
     */
    public static <T> T registerBean(Class<T> clazz, String beanName) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        //创建bean信息
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

        //返回注册的bean
        return applicationContext.getBean(beanName, clazz);
    }

    /*
     * @Description 根据bean名称移除bean
     * @Params ==>
     * @Param beanName
     * @Return void
     * @Date 2020/4/21 10:03
     * @Auther YINZHIYU
     */
    public static void removeBean(String beanName) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        //删除bean.
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    }
}