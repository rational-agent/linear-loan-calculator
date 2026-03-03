package com.rationalagent.loancalculator.core.calculator;

import com.rationalagent.loancalculator.core.calculator.AmortizationCalculator;
import com.rationalagent.loancalculator.core.calculator.LoanCalculator;
import com.rationalagent.loancalculator.core.dto.AmortizationSummary;
import com.rationalagent.loancalculator.core.dto.LoanDetails;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculatorTest {

    LoanDetails spec = new LoanDetails(
            new BigDecimal("500000.00"),
            new BigDecimal("1.75"),
            LocalDate.of(2020, 1, 15),
            LocalDate.of(2050, 1, 1),
            15
    );

    @Test
    void shouldCalculateTermInMonths() {
        assertEquals(361, AmortizationCalculator.calculateAmortizationSchedule(spec).size());
    }

    // TODO: create separate method for the amortization summary
    @Test
    void shouldCalculateAmortizationSummary() {
        AmortizationSummary as = LoanCalculator.calculateLoan(spec).getAmortizationSummary();
        assertEquals(spec.principal(), as.loanAmount());
    }

    @Test
    void dayOfMonthShouldBeEqualToPayDay() {
        assertEquals(spec.payDay(),
                LoanCalculator.calculateLoan(spec).getAmortizationSchedule()
                        .stream()
                        .map(payment -> payment.paymentDate().getDayOfMonth())
                        .distinct()
                        .toList()
                        .getFirst());
    }
}
