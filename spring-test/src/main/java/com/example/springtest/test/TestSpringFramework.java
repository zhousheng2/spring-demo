package com.example.springtest.test;

import com.example.springframework.beans.config.ClassPathResource;
import com.example.springframework.beans.config.Resource;
import com.example.springframework.beans.factory.DefaultListableBeanFactory;
import com.example.springframework.beans.factory.BeanFactory;
import com.example.springtest.pojo.Student;
import org.junit.Test;

/**
 * @Despriction: 测试手写Spring框架
 * @Author: zhousheng
 * @CreatedTime: 2019-07-09 16:33
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public class TestSpringFramework {


    @Test
    public void test() throws Exception {
        // 1、指定资源文件路径
        String location = "classpath:beans.xml";
//        Resource resource = new ClassPathResource(location);
        // 2、创建工厂（简单工厂模式）
        BeanFactory beanFactory = new DefaultListableBeanFactory(location);
//        BeanFactory beanFactory = new DefaultListableBeanFactory(resource);
        // 3、从工厂中获取指定对象
        Student student = (Student) beanFactory.getBean("student");
        // 4、测试对象是否可用
        System.out.println(student);

    }


}
