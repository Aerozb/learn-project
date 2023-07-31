package com.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfig {

    @Value("${demo}")
    private String demo;

    @Bean
    public DemoService demoService(){
        return new DemoService(demo);
    }
}