package com.example.springframework.beans.factory;

import com.example.springframework.beans.aware.Aware;
import com.example.springframework.beans.aware.BeanFactoryAware;
import com.example.springframework.beans.config.*;
import com.example.springframework.beans.converter.IntegerTypeConverter;
import com.example.springframework.beans.converter.StringTypeConverter;
import com.example.springframework.beans.converter.TypeConverter;
import com.example.springframework.beans.utils.ReflectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Despriction: BeanFactory底层实现
 * @Author: zhousheng
 * @CreatedTime: 2019-07-09 16:46
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public class DefaultListableBeanFactory extends AbstractBeanFactory {

    /**
     * 读取的Bean信息存储的BeanDefination信息
     */
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();// Map中的数据怎么来？
    private Map<String, Object> singletonObjects = new HashMap<>();// Map中的数据怎么来？

    private List<Resource> resources = new ArrayList<>();

    private List<TypeConverter> converters = new ArrayList<>();

    public DefaultListableBeanFactory(String location) {

        // 注册资源类
        registeResources();
        registeTypeConverters();
        // 因为不清楚location字符串到底代表的是类路径下的xml还是D盘下的xml还是网络中的xml
        Resource resource = getResource(location);

        XmlBeanDefinationParser xmlBeanDefinationParser = new XmlBeanDefinationParser();
        xmlBeanDefinationParser.loadBeanDefinations(this, resource);
    }

    private void registeTypeConverters() {
        converters.add(new StringTypeConverter());
        converters.add(new IntegerTypeConverter());
    }

    private void registeResources() {
        resources.add(new ClassPathResource());
    }

    private Resource getResource(String location) {
        for (Resource resource : resources) {
            if (resource.isCanRead(location)) {
                return resource;
            }
        }
        return null;
    }


    @Override
    public Object getBean(String beanName) {
        //    // 优化方案
        //    // 给对象起个名，在xml配置文件中，建立名称和对象的映射关系
        //	  1.如果singletonObjects中已经包含了我们要找的对象，就不需要再创建了。
        //    2.如果singletonObjects中已经没有包含我们要找的对象，那么根据传递过来的beanName参数去BeanDefinition集合中查找对应的BeanDefinition信息
        //	  3.根据BeanDefinition中的信息去创建Bean的实例。
        //    a)根据class信息包括里面的constructor-arg通过反射进行实例化
        //    b)根据BeanDefinition中封装的属性信息集合去挨个给对象赋值。
        //    类型转换
        //            反射赋值
        //    c)根据initMethod方法去调用对象的初始化操作

        Object instance = singletonObjects.get(beanName);
        if (instance != null) {
            return instance;
        }
        // 只处理单例
        // 先获取BeanDefinition
        BeanDefinition beanDefinition = this.beanDefinitions.get(beanName);
        String beanClassName = beanDefinition.getBeanClassName();
        // 通过构造参数创建Bean的实例
        instance = createBeanInstance(beanClassName, null);

        // 设置参数
        setProperty(instance, beanDefinition);

        // 初始化
        initBean(instance, beanDefinition);

        //将创建的对象放入缓存中
        singletonObjects.put(beanName, instance);

        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getBeansByType(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            for (BeanDefinition beanDefinition : beanDefinitions.values()) {
                Class<?> aClass = Class.forName(beanDefinition.getBeanClassName());
                // clazz是否是aClass的父类型
                if (clazz.isAssignableFrom(aClass)) {
                    String beanName = beanDefinition.getBeanName();
                    list.add((T)getBean(beanName));
                }
            }
            return list;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getBeanNamesByType(Class<?> clazz) {
        List<String> beanNamelist = new ArrayList<>();
        try {
            for (String beanName : beanDefinitions.keySet()) {
                BeanDefinition beanDefinition = beanDefinitions.get(beanName);
                Class<?> aClass = Class.forName(beanDefinition.getBeanClassName());
                // clazz是否是aClass的父类型
                if (clazz.isAssignableFrom(aClass)) {
                    beanNamelist.add(beanName);
                }
            }
            return beanNamelist;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    /**
     * 执行初始化方法
     * @param instance
     * @param beanDefinition
     */
    private void initBean(Object instance, BeanDefinition beanDefinition) {
        // 判断aware是不是instance实例的接口
        // 操作Aware接口
        if (instance instanceof Aware) {
            if (instance instanceof BeanFactoryAware) {
                ((BeanFactoryAware) instance).setBeanFactory(this);
            }
        }
        String initMethod = beanDefinition.getInitMethod();
        if (initMethod == null || "".equals(initMethod)) {
            return;
        }
        // 执行初始化方法
        ReflectUtils.invokeMethod(instance,initMethod);
    }

    /**
     * 设置参数
     * @param instance
     * @param beanDefinition
     */
    private void setProperty(Object instance, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            // value的类型可能是TypedStringValue或者是RuntimeBeanReference
            Object valueToUse = null;
            if (value instanceof TypedStringValue) {
                TypedStringValue typedStringValue = (TypedStringValue)value;
                String stringValue = typedStringValue.getValue();
                Class<?> targetType = typedStringValue.getTargetType();
                for (TypeConverter converter : converters) {
                    if (converter.isType(targetType)) {
                        valueToUse = converter.convert(stringValue);
                    }
                }
            } else if (value instanceof RuntimeBeanReference) {
                RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference)value;
                String ref = runtimeBeanReference.getRef();
                // 递归调用getBean方法
                valueToUse = getBean(ref);
            }
            // 调用反射进行赋值
            ReflectUtils.setProperty(instance, name, valueToUse);
        }
    }

    /**
     * 通过反射构造实例
     * @param beanClassName
     * @param object
     * @return
     */
    private Object createBeanInstance(String beanClassName, Object...object) {
        return ReflectUtils.createObject(beanClassName,object);
    }


    public Map<String, BeanDefinition> getBeanDefinations() {
        return beanDefinitions;
    }

    public void addBeanDefinations(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName, beanDefinition);
    }

    /**
     *往Map中存储BeanDefinition
     * @param beanName
     * @param beanDefinition
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName, beanDefinition);
    }
}
