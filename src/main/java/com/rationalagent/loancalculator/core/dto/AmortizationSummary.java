package com.rationalagent.loancalculator.core.dto;

import java.math.BigDecimal;

public record AmortizationSummary(BigDecimal totalAmount, BigDecimal loanAmount, BigDecimal totalInterest) {
}
