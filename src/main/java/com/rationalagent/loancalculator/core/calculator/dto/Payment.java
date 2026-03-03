package com.rationalagent.loancalculator.core.calculator.dto;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class Payment {

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

}
