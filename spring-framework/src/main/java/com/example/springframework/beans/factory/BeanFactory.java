package com.example.springframework.beans.factory;

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
}
