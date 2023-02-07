package com.zzq.gulimall.cart;

import com.zzq.common.config.GulimallSessionConfig;
import com.zzq.common.config.MyThreadConfig;
import com.zzq.common.config.ThreadPoolConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Import({GulimallSessionConfig.class, MyThreadConfig.class, ThreadPoolConfigProperties.class})
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallCartApplication.class, args);
	}

}
