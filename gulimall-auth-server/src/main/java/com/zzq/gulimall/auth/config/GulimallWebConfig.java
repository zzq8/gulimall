package com.zzq.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 发送一个请求直接跳转到一个页面。
 * SpringMVC vicwcontroller;将请求和页面映射过来
 * 视图映射：请求直接跳转页面的，用这种方式！不写空方法了
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    /**
     *     @GetMapping("/login.html")
     *     public String loginPage(){
     *         return "login";
     *     }
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
