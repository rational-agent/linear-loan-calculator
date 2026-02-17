package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.AmortizationSummary;
import com.rationalagent.loancalculator.loan.repository.model.LoanDetails;
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
        assertEquals(spec.getPrincipal(), as.getLoanAmount());
    }

    @Test
    void dayOfMonthShouldBeEqualToPayDay() {
        assertEquals(spec.getPayDay(),
                LoanCalculator.calculateLoan(spec).getAmortizationSchedule()
                        .stream()
                        .map(payment -> payment.getPaymentDate().getDayOfMonth())
                        .distinct()
                        .toList()
                        .getFirst());
    }
}
