package com.nibin.libray_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Libray1Application {

	public static void main(String[] args) {
		SpringApplication.run(Libray1Application.class, args);
	}

}
