package com.example.springframework.beans.factory;

import com.example.springframework.beans.config.BeanDefinition;

import java.util.List;
import java.util.Map;

/**
 * @Despriction: bean工厂
 * @Author: zhousheng
 * @CreatedTime: 2019-07-09 16:41
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public interface BeanFactory {

    /**
     * 根据bean的name获取对象
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    /**
     * 根据bean的类型获取对象
     *
     * @param clazz
     * @return
     */
    Object getBean(Class<?> clazz);

    /**
     * 根据指定bean的类型，获取对应的类型和子类型对应的bean实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getBeansByType(Class<T> clazz);

    /**
     * 根据指定bean的类型，获取对应的类型和子类型对应的bean名称
     *
     * @param clazz
     * @return
     */
    List<String> getBeanNamesByType(Class<?> clazz);

    /**
     * 获取所有BeanDefinition集合
     *
     * @return
     */
    Map<String, BeanDefinition> getBeanDefinitions();
}
