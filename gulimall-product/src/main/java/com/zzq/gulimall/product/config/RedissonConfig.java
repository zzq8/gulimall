package com.zzq.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient getRedissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://101.34.55.204:6379").setPassword("547061946");
        return Redisson.create(config);
    }
}
