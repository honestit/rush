package com.github.rush.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderingServerApplication.class, args);
    }
}
