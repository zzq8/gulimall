package com.zzq.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@EnableConfigurationProperties(ThreadPoolConfigProperties.class)  //如果这个类是第三方jar没在容器中可这样
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor getPool(ThreadPoolConfigProperties properties){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(properties.getCoreSize(),
                properties.getMaxSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000), //这里注意要填 capacity 不填默认 this(Integer.MAX_VALUE); 内存不够
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());//抛出异常，并且丢弃掉任务

        return threadPoolExecutor;
    }
}
