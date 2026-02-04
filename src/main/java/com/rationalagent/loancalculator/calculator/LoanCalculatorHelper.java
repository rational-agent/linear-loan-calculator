package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.LoanSpecification;
import com.rationalagent.loancalculator.loan.repository.model.Payment;
import com.rationalagent.loancalculator.loan.repository.model.PaymentType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

class LoanCalculatorHelper {

    static BigDecimal recalculatePrincipalBalance(BigDecimal balance, BigDecimal payment) {
        if (balance.compareTo(payment) < 0) {
            return payment;
        } else {
            return balance.subtract(payment);
        }
    }


    static Integer calculateTermInMonths(LoanSpecification spec) {
        return LoanCalculatorHelper.calculateLoanLifeInMonths(spec.getStartDate(), spec.getEndDate());
    }

    static BigDecimal calculateMonthlyInterestRate(BigDecimal yearlyInterestRate) {
        return yearlyInterestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
    }

    static BigDecimal getMonthWeight(LocalDate date) {
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

    static Payment calculateFirstPrincipalPayment(LoanSpecification spec) {
        var startDateWeight = LoanCalculatorHelper.getMonthWeight(spec.getStartDate());
        var regularPayment = calculatePrincipalPayment(spec);
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, regularPayment.getPaymentUnrounded().multiply(startDateWeight));
    }

    static Payment calculatePrincipalPayment(LoanSpecification spec) {
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, spec.getPrincipal().divide(new BigDecimal(calculateTermInMonths(spec)), 10, RoundingMode.HALF_EVEN));
    }

    static BigDecimal calculateMonthlyInterest(BigDecimal remainingPrincipal, BigDecimal decimalInterest) {
        return remainingPrincipal.multiply(LoanCalculatorHelper.calculateMonthlyInterestRate(decimalInterest));
    }

    private static int calculateLoanLifeInMonths(LocalDate startDate, LocalDate endDate) {
        var firstDay = startDate.with(TemporalAdjusters.firstDayOfMonth());
        var lastDate = endDate.with(TemporalAdjusters.firstDayOfMonth());

        return (int) ChronoUnit.MONTHS.between(firstDay, lastDate);
    }
}
