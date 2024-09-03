package com.schizoscrypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayRunner {
    public static void main(String[] args) {
        SpringApplication.run(GatewayRunner.class, args);
    }
}