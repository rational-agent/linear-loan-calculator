package com.rationalagent.linearloancalculator.core.calculator;

import com.rationalagent.linearloancalculator.core.dto.LoanDetails;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculatorTest {

    LoanDetails loanDetails = new LoanDetails(
            new BigDecimal("500000.00"),
            new BigDecimal("1.75"),
            LocalDate.of(2020, 1, 15),
            LocalDate.of(2050, 1, 1),
            15
    );

    @Test
    void shouldCalculateTermInMonths() {
        assertEquals(361, AmortizationCalculator.calculatePaymentSchedule(loanDetails).size());
    }

    @Test
    void dayOfMonthShouldBeEqualToPayDay() {
        assertEquals(loanDetails.payDay(),
                LoanCalculator.calculateLoan(loanDetails).getPaymentSchedule()
                        .stream()
                        .map(payment -> payment.getPaymentDate().getDayOfMonth())
                        .distinct()
                        .toList()
                        .getFirst());
    }
}
