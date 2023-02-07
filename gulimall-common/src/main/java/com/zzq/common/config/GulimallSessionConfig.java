package com.zzq.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@EnableRedisHttpSession //创建了一个springSessionRepositoryFilter ，负责将原生HttpSession 替换为Spring Session的实现
//@EnableRedisHttpSession  //这个在common里加没用  （又好像有用），理解：Enable感觉是使得application类路径下的包用setsession有这个功能
@Configuration
public class GulimallSessionConfig {

    /**
     * 解决Domain域
     */
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        //放大作用域
        cookieSerializer.setDomainName("gulimall.com");
        cookieSerializer.setCookieName("GULISESSION");

        return cookieSerializer;
    }


    /**
     * 解决序列化
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        ParserConfig.getGlobalInstance().addAccept("com.zzq.gulimall.");
//        return new GenericFastJsonRedisSerializer();
        return new GenericJackson2JsonRedisSerializer();
    }
}
