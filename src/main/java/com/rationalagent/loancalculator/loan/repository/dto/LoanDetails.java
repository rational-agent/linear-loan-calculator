package com.rationalagent.loancalculator.loan.repository.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record LoanDetails(
        BigDecimal principal,
        BigDecimal interestRate,
        LocalDate startDate,
        LocalDate endDate,
        Integer payDay) {
}
