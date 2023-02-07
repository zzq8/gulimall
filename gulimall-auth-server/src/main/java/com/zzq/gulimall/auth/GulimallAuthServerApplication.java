package com.zzq.gulimall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Import(GulimallSessionConfig.class) //我的理解是，配置类得放主类能扫描到的地方才生效
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallAuthServerApplication.class, args);
	}

}
