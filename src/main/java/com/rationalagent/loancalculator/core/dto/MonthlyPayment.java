package com.rationalagent.loancalculator.core.dto;


import java.math.BigDecimal;
import java.time.LocalDate;


public record MonthlyPayment(
        LocalDate paymentDate,
        BigDecimal principalPayment,
        BigDecimal interestPayment,
        BigDecimal remainingLoanAmount) {
}