package com.rationalagent.loancalculator.core.calculator;

import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.Payment;
import com.rationalagent.loancalculator.core.dto.PaymentType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class LoanCalculatorHelper {

    static BigDecimal recalculatePrincipalBalance(BigDecimal balance, BigDecimal payment) {
        if (balance.compareTo(payment) < 0) {
            return payment;
        } else {
            return balance.subtract(payment);
        }
    }

    static Integer calculateTermInMonths(LoanDetails spec) {
        return LoanCalculatorHelper.calculateLoanLifeInMonths(spec.startDate(), spec.endDate());
    }

    static BigDecimal calculateMonthlyInterestRate(BigDecimal yearlyInterestRate) {
        return yearlyInterestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
    }

    static Payment calculateFirstPrincipalPayment(LoanDetails loanDetails) {
        var startDateWeight = getMonthWeight(loanDetails.startDate());
        var regularPayment = calculatePrincipalPayment(loanDetails);
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, regularPayment.getPaymentUnrounded().multiply(startDateWeight));
    }

    static Payment calculatePrincipalPayment(LoanDetails loanDetails) {
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, loanDetails.principal().divide(new BigDecimal(calculateTermInMonths(loanDetails)), 10, RoundingMode.HALF_EVEN));
    }

    static BigDecimal calculateMonthlyInterest(BigDecimal remainingPrincipal, BigDecimal decimalInterest) {
        return remainingPrincipal.multiply(LoanCalculatorHelper.calculateMonthlyInterestRate(decimalInterest));
    }

    private static BigDecimal getMonthWeight(LocalDate date) {
        if (date.getDayOfMonth() == 1) {
            return BigDecimal.ONE;
        } else {
            var firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
            var lastDate = date.with(TemporalAdjusters.lastDayOfMonth());
            var amountOfDaysInMonth = Duration.between(firstDay.atStartOfDay(), lastDate.atStartOfDay()).toDays();
            var amountOfDaysInBetween = Duration.between(date.atStartOfDay(), lastDate.atStartOfDay()).toDays();

            return BigDecimal.valueOf((1.0 / amountOfDaysInMonth) * amountOfDaysInBetween);
        }
    }

    private static int calculateLoanLifeInMonths(LocalDate startDate, LocalDate endDate) {
        var firstDay = startDate.with(TemporalAdjusters.firstDayOfMonth());
        var lastDate = endDate.with(TemporalAdjusters.firstDayOfMonth());

        return (int) ChronoUnit.MONTHS.between(firstDay, lastDate);
    }
}
