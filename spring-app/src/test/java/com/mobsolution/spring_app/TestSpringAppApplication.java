package com.mobsolution.spring_app;

import org.springframework.boot.SpringApplication;

public class TestSpringAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
