package com.bank.ekyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application runner for eKYC Onboarding Service.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@SpringBootApplication
public class EkycApplication {

    /**
     * Start execution of the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EkycApplication.class, args);
    }
}
