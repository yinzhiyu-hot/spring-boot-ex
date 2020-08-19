package com.example.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Description 类工具
 * @PackagePath com.example.common.utils.ClassUtil
 * @Author YINZHIYU
 * @Date 2020/6/17 10:34
 * @Version 1.0.0.0
 **/
@SuppressWarnings("unchecked")
public class ClassUtils {

    /*
     * @Description 获取同一路径下所有子类或接口实现类
     * springboot 打成jar后，无法获取，包目录结构变化了，idea运行倒是可用，时间紧，不做过多研究了
     * @Params ==>
     * @Param cls
     * @Return java.util.List<java.lang.Class<?>>
     * @Date 2020/6/17 10:37
     * @Auther YINZHIYU
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls) {
        List<Class<?>> classes = Lists.newArrayList();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !Objects.equals(cls, c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /*
     * @Description 取得当前类路径下的所有类
     * springboot 打成jar后，无法获取，包目录结构变化了，idea运行倒是可用，时间紧，不做过多研究了
     * @Params ==>
     * @Param cls
     * @Return java.util.List<java.lang.Class<?>>
     * @Date 2020/6/17 10:37
     * @Auther YINZHIYU
     */
    public static List<Class<?>> getClasses(Class<?> cls) {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    /*
     * @Description 迭代查找类
     * springboot 打成jar后，无法获取，包目录结构变化了，idea运行倒是可用，时间紧，不做过多研究了
     * @Params ==>
     * @Param dir
     * @Param pk
     * @Return java.util.List<java.lang.Class<?>>
     * @Date 2020/6/17 15:02
     * @Auther YINZHIYU
     */
    private static List<Class<?>> getClasses(File dir, String pk) {
        List<Class<?>> classes = Lists.newArrayList();
        try {
            if (!dir.exists()) {
                return classes;
            }
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    classes.addAll(getClasses(f, pk + "." + f.getName()));
                }
                String name = f.getName();
                if (name.endsWith(".class")) {
                    classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
                }
            }
        } catch (Exception e) {
            LogUtils.error(dir, pk, "迭代查找类处理异常", e);
        }
        return classes;
    }

    /*
     * @Description 获取在指定包下某个class的所有非抽象子类
     * 此方法在springboot 打成jar后，可以获取，终于搞出来了
     * @Params ==>
     * @Param parentClass 父类
     * @Param packagePath 指定包，格式如"cn.wangoon.service.job"
     * @Return java.util.List<java.lang.Class<E>> 该父类对应的所有子类列表
     * @Date 2020/6/17 15:01
     * @Auther YINZHIYU
     */
    public static <E> List<Class<E>> getSubClasses(final Class<E> parentClass, final String packagePath) {
        final List<Class<E>> subClasses = Lists.newArrayList();
        try {
            final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AssignableTypeFilter(parentClass));
            final Set<BeanDefinition> components = provider.findCandidateComponents(packagePath);
            for (final BeanDefinition component : components) {
                final Class<E> cls = (Class<E>) Class.forName(component.getBeanClassName());
                if (Modifier.isAbstract(cls.getModifiers())) {
                    continue;
                }
                subClasses.add(cls);
            }
        } catch (Exception e) {
            LogUtils.error(parentClass, packagePath, "获取在指定包下某个class的所有非抽象子类异常", e);
        }
        return subClasses;
    }

    /*
     * @Description 获取指定类的指定了beanName的子类的class Map
     * @Params ==>
     * @Param clazz
     * @Return java.util.Map<java.lang.String,java.lang.Class<?>>
     * @Date 2020/6/17 10:40
     * @Auther YINZHIYU
     */
    public static Map<String, Class<?>> getSuperClassMapForComponent(Class clazz) {
        Map<String, Class<?>> stringClassMap = Maps.newHashMap();
        List<Class<?>> classArrayList = Lists.newArrayList();
        classArrayList.addAll(getSubClasses(clazz, clazz.getPackage().getName()));
        for (Class<?> clazzTemp : classArrayList) {
            if (ObjectUtil.isNotEmpty(clazzTemp.getAnnotation(Component.class))) {
                stringClassMap.put(clazzTemp.getAnnotation(Component.class).value(), clazzTemp);
            }
        }
        return stringClassMap;
    }
}