package com.rationalagent.loancalculator.core.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


@Setter
@Getter
public class LoanSummary {

    private BigDecimal principalAmount;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;

    public LoanSummary(BigDecimal principalAmount, BigDecimal totalInterest) {
        this.principalAmount = principalAmount;
        this.totalInterest = totalInterest;
        this.totalAmount = principalAmount.add(totalInterest);
    }

}
