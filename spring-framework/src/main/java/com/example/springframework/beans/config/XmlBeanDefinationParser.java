package com.example.springframework.beans.config;

import com.example.springframework.beans.factory.DefaultListableBeanFactory;
import com.example.springframework.beans.utils.DocumentReader;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * @Despriction: TODO
 * @Author: zhousheng
 * @CreatedTime: 2019-07-09 17:11
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public class XmlBeanDefinationParser {

    public void loadBeanDefinations(DefaultListableBeanFactory defaultListableBeanFactory, Resource resource) {

        InputStream inputStream = resource.getInputStream();
        Document document = DocumentReader.creatDocument(inputStream);
        XmlBeanDefinationDocumentParser xmlBeanDefinationDocumentParser = new XmlBeanDefinationDocumentParser(defaultListableBeanFactory);
        xmlBeanDefinationDocumentParser.loadBeanDefinations(document.getRootElement());
    }
}
