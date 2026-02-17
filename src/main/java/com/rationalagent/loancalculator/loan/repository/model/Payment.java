package com.rationalagent.loancalculator.loan.repository.model;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private PaymentType type;
    private BigDecimal paymentUnrounded;
    private BigDecimal paymentRounded;
    private BigDecimal paymentRoundingError;

    public Payment(PaymentType type, BigDecimal principal) {
        this.type = type;
        this.paymentUnrounded = principal;
        this.paymentRounded = principal.setScale(2, RoundingMode.HALF_EVEN);
        this.paymentRoundingError = paymentUnrounded.subtract(paymentRounded);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public BigDecimal getPaymentUnrounded() {
        return paymentUnrounded;
    }

    public void setPaymentUnrounded(BigDecimal paymentUnrounded) {
        this.paymentUnrounded = paymentUnrounded;
    }

    public BigDecimal getPaymentRounded() {
        return paymentRounded;
    }

    public void setPaymentRounded(BigDecimal paymentRounded) {
        this.paymentRounded = paymentRounded;
    }

    public BigDecimal getPaymentRoundingError() {
        return paymentRoundingError;
    }

    public void setPaymentRoundingError(BigDecimal paymentRoundingError) {
        this.paymentRoundingError = paymentRoundingError;
    }
}
