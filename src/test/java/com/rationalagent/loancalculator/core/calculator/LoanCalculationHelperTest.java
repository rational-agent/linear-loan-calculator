package com.rationalagent.loancalculator.core.calculator;

import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.PaymentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculationHelperTest {

    @Test
    void shouldRecalculatePrincipalBalance_BalanceGreaterThanPayment() {
        var balance = BigDecimal.valueOf(1000.00);
        var payment = BigDecimal.valueOf(200.00);

        var result = LoanCalculatorHelper.recalculatePrincipalBalance(balance, payment);

        assertEquals(BigDecimal.valueOf(800.00), result);
    }

    @Test
    void shouldRecalculatePrincipalBalance_BalanceLessThanPayment() {
        var balance = BigDecimal.valueOf(100.00);
        var payment = BigDecimal.valueOf(200.00);

        var result = LoanCalculatorHelper.recalculatePrincipalBalance(balance, payment);

        assertEquals(payment, result);
    }

    @Test
    void shouldCalculatePrincipalPayment() {
        var loanDetails = getLoanDetails();

        var payment = LoanCalculatorHelper.calculatePrincipalPayment(loanDetails);

        assertEquals(PaymentType.PRINCIPAL_PAYMENT, payment.getType());
        assertEquals(BigDecimal.valueOf(416.67), payment.getPaymentRounded());
    }

    @Test
    void shouldCalculateFirstPrincipalPayment_FirstDayOfMonth() {
        var loanDetails = getLoanDetails();

        var payment = LoanCalculatorHelper.calculateFirstPrincipalPayment(loanDetails);

        assertEquals(BigDecimal.valueOf(416.67), payment.getPaymentRounded());
    }

    @Test
    void shouldCalculateMonthlyInterest() {
        var remainingPrincipal = BigDecimal.valueOf(1000);
        var decimalInterest = BigDecimal.valueOf(0.12);

        var interest = LoanCalculatorHelper.calculateMonthlyInterest(remainingPrincipal, decimalInterest);

        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(interest));
    }

    private static LoanDetails getLoanDetails() {
        return new LoanDetails(
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(0.05),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2050, 1, 1),
                1
        );
    }
}
