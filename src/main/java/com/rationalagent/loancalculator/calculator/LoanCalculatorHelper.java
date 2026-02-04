package com.rationalagent.loancalculator.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

class LoanCalculatorHelper {

    private LoanCalculatorHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static final BigDecimal MONTHS_IN_A_YEAR = new BigDecimal("12.00");

    static int calculateLoanLifeInMonths(LocalDate startDate, LocalDate endDate) {
        var firstDay = startDate.with(TemporalAdjusters.firstDayOfMonth());
        var lastDate = endDate.with(TemporalAdjusters.firstDayOfMonth());

        return (int) ChronoUnit.MONTHS.between(firstDay, lastDate);
    }

    static BigDecimal calculateMonthlyInterestRate(BigDecimal yearlyInterestRate) {
        return yearlyInterestRate.divide(MONTHS_IN_A_YEAR, 10, RoundingMode.HALF_EVEN);
    }

    static BigDecimal getRoundedAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    static BigDecimal getMonthWeight(LocalDate date) {
        if(isAtStartOfMonth(date)) {
            return BigDecimal.ONE;
        } else {
            var firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
            var lastDate = date.with(TemporalAdjusters.lastDayOfMonth());
            var amountOfDaysInMonth = Duration.between(firstDay.atStartOfDay(), lastDate.atStartOfDay()).toDays();
            var amountOfDaysInBetween = Duration.between(date.atStartOfDay(), lastDate.atStartOfDay()).toDays();

            return BigDecimal.valueOf((1.0 / amountOfDaysInMonth) * amountOfDaysInBetween);
        }
    }

    private static boolean isAtStartOfMonth(LocalDate date) {
        return date.getDayOfMonth() == 1;
    }

}
