package com.example.springframework.beans.aware;

import com.example.springframework.beans.factory.BeanFactory;

/**
 * @Despriction: TODO
 * @Author: zhousheng
 * @CreatedTime: 2019-07-24 16:14
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
