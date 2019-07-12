package com.example.springframework.beans.config;

import com.example.springframework.beans.factory.DefaultListableBeanFactory;
import com.example.springframework.beans.utils.ReflectUtils;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Despriction: TODO
 * @Author: zhousheng
 * @CreatedTime: 2019-07-10 16:40
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public class XmlBeanDefinationDocumentParser {

    private DefaultListableBeanFactory defaultListableBeanFactory;

    public XmlBeanDefinationDocumentParser(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.defaultListableBeanFactory = defaultListableBeanFactory;
    }

    public void loadBeanDefinations(Element rootElement) {
        //        2.将bean信息封装到BeanDefinition对象中
//                对bean标签解析解析
//                class信息
//                id信息
//                init-method信息
//                property标签信息----PropertyValue对象（name和value）
//                      name信息
//                      value信息
//                          value属性---属性值、属性类型（属性赋值的时候，需要进行类型转换）TypedStringValue
//                          ref属性---RuntimeBeanReference(bean的名称)---根据bean的名称获取bean的实例，将获取到的实例赋值该对象
//        3.再将BeanDefinition放入集合对象中
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            //获取标签名称
            String name = element.getName();
            if ("bean".equals(name)) {
                parseDefaultElement(element);
            } else {
                parseCustomElement(element);
            }
        }
    }

    /**
     * 解析<element>标签
     * @param element
     */
    private void parseDefaultElement(Element element) {
        try {
            if (element == null){
                return;
            }
            //获取id属性
            String id = element.attributeValue("id");
            String name = element.attributeValue("name");
            String clazz = element.attributeValue("class");
            Class<?> classType = Class.forName(clazz);
            String initMethod = element.attributeValue("init-method");
            //有id则以id为主，没有则以name为主
            String beanName = id == null ? name : id;
            beanName = beanName == null ? classType.getSimpleName() : beanName;

            // 创建BeanDefinition对象
            BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz);
            beanDefinition.setInitMethod(initMethod);

            List<Element> propertyElements = element.elements();
            for (Element propertyElement : propertyElements) {
                parsePropertyElement(beanDefinition, propertyElement);
            }

            // 注册BeanDefinition信息
            registerBeanDefinition(beanName, beanDefinition);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册BeanDefinition
     * @param beanName
     * @param beanDefinition
     */
    private void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 解析<property>标签
     * @param beanDefinition
     * @param propertyElement
     */
    private void parsePropertyElement(BeanDefinition beanDefinition, Element propertyElement) {
        if (propertyElement == null) {
            return;
        }
        // 获取name属性
        String name = propertyElement.attributeValue("name");
        // 获取value属性
        String value = propertyElement.attributeValue("value");
        // 获取ref属性
        String ref = propertyElement.attributeValue("ref");
        //若value和ref都有值，则直接返回
        if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(ref)) {
            return;
        }
        //封装一个<property>的内容
        PropertyValue propertyValue = null;
        if (!StringUtils.isEmpty(value)) {
            TypedStringValue typedStringValue = new TypedStringValue(value);
            Class<?> typeByFieldName = ReflectUtils.getTypeByFieldName(beanDefinition.getBeanClassName(), name);
            typedStringValue.setTargetType(typeByFieldName);
            propertyValue = new PropertyValue(name, typedStringValue);
            beanDefinition.addPropertyValues(propertyValue);
        } else if (!StringUtils.isEmpty(ref)) {
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(ref);
            propertyValue = new PropertyValue(name, runtimeBeanReference);
            beanDefinition.addPropertyValues(propertyValue);
        } else {
            return;
        }
    }

    private void parseCustomElement(Element element) {
    }
}
