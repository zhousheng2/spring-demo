package com.example.springframework.beans.config;

import java.io.InputStream;

/**
 * @Despriction: TODO
 * @Author: zhousheng
 * @CreatedTime: 2019-07-10 16:20
 * @ModifyBy:
 * @ModifyTime:
 * @ModifyDespriction:
 * @Version: V1.0.0
 */
public interface Resource {

    boolean isCanRead(String location);

    InputStream getInputStream();
}
