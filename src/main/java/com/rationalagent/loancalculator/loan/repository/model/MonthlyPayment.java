package com.rationalagent.loancalculator.loan.repository.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class MonthlyPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate paymentDate;
    // Portion of your monthly payment that reduces the principal balance of a home loan. This term also refers to prepayments you make to the principal balance.
    private BigDecimal principalPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingLoanAmount;

    public MonthlyPayment() {
    }

    public MonthlyPayment(LocalDate paymentDate, BigDecimal principalPayment, BigDecimal interestPayment, BigDecimal remainingLoanAmount) {
        this.paymentDate = paymentDate;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.remainingLoanAmount = remainingLoanAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPrincipalPayment() {
        return principalPayment;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getRemainingLoanAmount() {
        return remainingLoanAmount;
    }

    public void setRemainingLoanAmount(BigDecimal remainingLoanAmount) {
        this.remainingLoanAmount = remainingLoanAmount;
    }
}