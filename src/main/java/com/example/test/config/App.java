package com.example.test.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * Project: SrpingBootTest
 * Create User: TRS-chen
 * Create Time:2018/8/4 18:10
 */
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
//exclude = {JpaRepositoriesAutoConfiguration.class,//禁止springboot自动加载持久化bean
@ComponentScan(basePackages = {"com.example.test"})
@EnableEurekaClient
@EnableFeignClients
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
