package com.zzq.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@Import({GulimallSessionConfig.class, ThreadPoolConfigProperties.class, MyThreadConfig.class })
@EnableFeignClients(basePackages = "com.zzq.gulimall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallProductApplication.class, args);
	}

}
