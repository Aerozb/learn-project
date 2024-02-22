package com;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.dao")
public class Bank2ApplicationService {

	public static void main(String[] args) {
		SpringApplication.run(Bank2ApplicationService.class, args);
	}

}
