package com.somz.dev.RgstSomz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RgstSomzApplication {

	public static void main(String[] args) {

		SpringApplication.run(RgstSomzApplication.class, args);
	}

}
