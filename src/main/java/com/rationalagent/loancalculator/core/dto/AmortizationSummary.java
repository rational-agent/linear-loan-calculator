package com.rationalagent.loancalculator.core.dto;

import java.math.BigDecimal;

public record AmortizationSummary(
        BigDecimal totalAmount,
        BigDecimal loanAmount,
        BigDecimal loanAmountRoundingError,
        BigDecimal totalInterest,
        BigDecimal interestRoundingError) { }
