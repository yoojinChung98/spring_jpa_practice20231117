package com.study.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaApplication {

	public static void main(String[] args) {
		// 내장 톰캣을 가동시키는 구문.
		SpringApplication.run(JpaApplication.class, args);
	}

}
