package com.example.springframework.beans.factory;

/**
 * @Despriction: 空实现一下,可扩展
 * @Author: zhousheng
 * @CreatedTime: 2019-07-09 17:02
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return null;
    }
}
