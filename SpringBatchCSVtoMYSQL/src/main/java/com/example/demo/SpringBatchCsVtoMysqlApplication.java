package com.example.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})  // Changed from "com.org" to "com.example"
@EnableBatchProcessing
public class SpringBatchCsVtoMysqlApplication {
    public static void main(String[] args) {
        System.out.println("Hello Batch : ");
        SpringApplication.run(SpringBatchCsVtoMysqlApplication.class, args);
    }
}