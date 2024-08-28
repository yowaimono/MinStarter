package com.reservoir.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取Spring容器中的Bean
     *
     * @param beanName Bean的名称
     * @param <T>      Bean的类型
     * @return Bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        Assert.notNull(context, "ApplicationContext is not set");
        return (T) context.getBean(beanName);
    }

    /**
     * 获取Spring容器中的Bean
     *
     * @param beanClass Bean的类型
     * @param <T>       Bean的类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> beanClass) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.getBean(beanClass);
    }

    /**
     * 获取Spring容器中指定类型的所有Bean
     *
     * @param beanClass Bean的类型
     * @param <T>       Bean的类型
     * @return 指定类型的所有Bean实例
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.getBeansOfType(beanClass);
    }

    /**
     * 获取Spring容器中指定类型的所有Bean的名称
     *
     * @param beanClass Bean的类型
     * @param <T>       Bean的类型
     * @return 指定类型的所有Bean名称
     */
    public static <T> String[] getBeanNamesOfType(Class<T> beanClass) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.getBeanNamesForType(beanClass);
    }

    /**
     * 检查Spring容器中是否存在Bean
     *
     * @param beanName Bean的名称
     * @return 是否存在
     */
    public static boolean containsBean(String beanName) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.containsBean(beanName);
    }

    /**
     * 检查Bean是否为单例
     *
     * @param beanName Bean的名称
     * @return 是否为单例
     */
    public static boolean isSingleton(String beanName) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.isSingleton(beanName);
    }

    /**
     * 检查Bean是否为指定类型
     *
     * @param beanName  Bean的名称
     * @param beanClass Bean的类型
     * @return 是否为指定类型
     */
    public static boolean isTypeMatch(String beanName, Class<?> beanClass) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.isTypeMatch(beanName, beanClass);
    }

    /**
     * 获取Bean的类型
     *
     * @param beanName Bean的名称
     * @return Bean的类型
     */
    public static Class<?> getType(String beanName) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.getType(beanName);
    }

    /**
     * 获取Bean的别名
     *
     * @param beanName Bean的名称
     * @return Bean的别名数组
     */
    public static String[] getAliases(String beanName) {
        Assert.notNull(context, "ApplicationContext is not set");
        return context.getAliases(beanName);
    }

    /**
     * 获取当前的Spring应用上下文
     *
     * @return ApplicationContext实例
     */
    public static ApplicationContext getApplicationContext() {
        Assert.notNull(context, "ApplicationContext is not set");
        return context;
    }
}
