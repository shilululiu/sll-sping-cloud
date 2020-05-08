package com.sll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableEurekaServer//注册中心需要加上EnableEurekaServer注解
@EnableHystrix//开启Hystrix的熔断器功能
@EnableHystrixDashboard//图表监控注解
@EnableFeignClients//Feign消费者需要加上此注解
@EnableDiscoveryClient
@EnableZuulProxy//zuul网关
@EnableTurbine//开启Turbine
public class SllSpingCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SllSpingCloudApplication.class, args);
    }
}
