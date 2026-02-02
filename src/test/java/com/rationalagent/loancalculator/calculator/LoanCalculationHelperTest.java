package com.rationalagent.loancalculator.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class LoanCalculationHelperTest {

    @Test
    void test() {
        BigDecimal a = LoanCalculatorHelper.getMonthWeight(LocalDate.of(2020,5,10));
        Assertions.assertEquals(BigDecimal.valueOf(0.7), a);
    }

    @Test
    void test2() {
        BigDecimal a = LoanCalculatorHelper.getMonthWeight(LocalDate.of(2020,5,15));
        Assertions.assertEquals(BigDecimal.valueOf(0.5333333333333333), a);
    }
}
