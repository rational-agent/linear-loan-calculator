package com.rationalagent.linearloancalculator.core.dto;


import java.math.BigDecimal;
import java.time.LocalDate;


public record MonthlyPayment(
        LocalDate paymentDate,
        BigDecimal principalPayment,
        BigDecimal interestPayment,
        BigDecimal remainingLoanAmount) {
}