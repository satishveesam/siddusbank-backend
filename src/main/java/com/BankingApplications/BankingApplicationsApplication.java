package com.BankingApplications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplicationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApplicationsApplication.class, args);
        System.out.println("========================================");
        System.out.println("Banking Application Started Successfully!");
        System.out.println("Server running on: http://localhost:3636");
        System.out.println("API Base URL: http://localhost:3636/api");
        System.out.println("========================================");
    }
}