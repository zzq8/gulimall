package com.zzq.gulimall.product;

import com.zzq.common.config.GulimallSessionConfig;
import com.zzq.common.config.MyThreadConfig;
import com.zzq.common.config.ThreadPoolConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Import({GulimallSessionConfig.class, ThreadPoolConfigProperties.class, MyThreadConfig.class })
@EnableFeignClients(basePackages = "com.zzq.gulimall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallProductApplication.class, args);
	}

}
