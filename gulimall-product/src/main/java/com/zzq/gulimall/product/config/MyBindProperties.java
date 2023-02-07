package com.zzq.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.zzq")
@Data  //需要加这个yaml才有提醒
public class MyBindProperties {

    private String name;
    private String age;

}
