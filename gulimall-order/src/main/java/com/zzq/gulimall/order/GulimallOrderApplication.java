package com.zzq.gulimall.order;

import com.zzq.common.config.GulimallSessionConfig;
import com.zzq.common.config.MyThreadConfig;
import com.zzq.common.config.ThreadPoolConfigProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients
@Import({GulimallSessionConfig.class, ThreadPoolConfigProperties.class, MyThreadConfig.class })
@EnableDiscoveryClient
@EnableRabbit
@SpringBootApplication
public class GulimallOrderApplication {
     public static void main(String[] args) {
           SpringApplication.run(GulimallOrderApplication.class, args);
      }
}
