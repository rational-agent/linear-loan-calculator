package com.rationalagent.loancalculator.loan.repository.model;

import java.math.BigDecimal;

public class AmortizationSummary {

    private BigDecimal totalAmount;
    private BigDecimal loanAmount;
    private BigDecimal loanAmountRoundingError;
    private BigDecimal totalInterest;
    private BigDecimal interestRoundingError;


    public AmortizationSummary(BigDecimal totalAmount, BigDecimal loanAmount, BigDecimal loanRoundingError, BigDecimal totalInterest, BigDecimal interestRoundingError) {
        this.totalAmount = totalAmount;
        this.loanAmount = loanAmount;
        this.loanAmountRoundingError = loanRoundingError;
        this.totalInterest = totalInterest;
        this.interestRoundingError = interestRoundingError;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getLoanAmountRoundingError() {
        return loanAmountRoundingError;
    }

    public void setLoanAmountRoundingError(BigDecimal loanAmountRoundingError) {
        this.loanAmountRoundingError = loanAmountRoundingError;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getInterestRoundingError() {
        return interestRoundingError;
    }

    public void setInterestRoundingError(BigDecimal interestRoundingError) {
        this.interestRoundingError = interestRoundingError;
    }
}
