package com.rationalagent.loancalculator.loan.repository.dto;

import java.math.BigDecimal;

public record AmortizationSummary(
        BigDecimal totalAmount,
        BigDecimal loanAmount,
        BigDecimal loanAmountRoundingError,
        BigDecimal totalInterest,
        BigDecimal interestRoundingError) { }
