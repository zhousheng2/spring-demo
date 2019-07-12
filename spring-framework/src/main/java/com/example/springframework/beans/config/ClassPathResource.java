package com.example.springframework.beans.config;

import org.springframework.util.StringUtils;

import java.io.InputStream;

/**
 * @Despriction: TODO
 * @Author: zhousheng
 * @CreatedTime: 2019-07-10 16:22
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public class ClassPathResource implements Resource {

    private String location;

    public ClassPathResource() {
    }

    public ClassPathResource(String location) {
        this.location = location;
    }

    @Override
    public boolean isCanRead(String location) {
        if (StringUtils.isEmpty(location)) {
            return false;
        }
        if (location.startsWith("classpath:")) {
            this.location = location;
            return true;
        }
        return false;
    }

    @Override
    public InputStream getInputStream() {
        if (StringUtils.isEmpty(location)) {
            return null;
        }
        location = location.replace("classpath:", "");
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
