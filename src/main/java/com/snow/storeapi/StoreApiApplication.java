package com.snow.storeapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan({"com.snow.storeapi.mapper"})
@EnableWebMvc
public class StoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApiApplication.class, args);
	}
}