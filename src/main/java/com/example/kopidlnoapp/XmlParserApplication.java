package com.example.kopidlnoapp;

import com.example.kopidlnoapp.service.ApplicationInitializerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class XmlParserApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(XmlParserApplication.class, args);
		ApplicationInitializerService initializerService = context.getBean(ApplicationInitializerService.class);
		try {
			initializerService.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}