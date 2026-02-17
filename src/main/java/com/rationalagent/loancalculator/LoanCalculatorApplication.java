package com.rationalagent.loancalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * An application that calculates linear loans.
 *
 */
@SpringBootApplication
public class LoanCalculatorApplication {

    static void main(String[] args) {
        SpringApplication.run(LoanCalculatorApplication.class, args);
    }
}
