package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.AmortizationMethod;
import com.rationalagent.loancalculator.loan.repository.model.AmortizationSummary;
import com.rationalagent.loancalculator.loan.repository.model.LoanSpecification;
import com.rationalagent.loancalculator.loan.repository.model.MonthlyPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculatorImplTest {

    private LoanCalculator loanCalculator;

    LoanSpecification spec = new LoanSpecification(
            new BigDecimal("500000.00"),
            new BigDecimal("1.75"),
            LocalDate.of(2020, 1, 15),
            LocalDate.of(2050, 1, 1),
            15
    );

    @BeforeEach
    void beforeEach() {
        loanCalculator = new LoanCalculatorImpl();
    }

    @Test
    void shouldCalculateTermInMonths() {
        assertEquals(361, loanCalculator.calculateAmortizationSchedule(spec).size());
    }

    // TODO: create separate method for the amortization summary
    @Test
    void shouldCalculateAmortizationSummary() {
        AmortizationSummary as = loanCalculator.calculateLoan(spec).getAmortizationSummary();
        assertEquals(spec.getPrincipal(), as.getLoanAmount());
    }

    @Test
    void dayOfMonthShouldBeEqualToPayDay() {
        assertEquals(spec.getPayDay(),
                loanCalculator.calculateLoan(spec).getAmortizationSchedule()
                        .stream()
                        .map(payment -> payment.getPaymentDate().getDayOfMonth())
                        .distinct()
                        .toList()
                        .getFirst());
    }

    @Test
    void allInterestAmountsShouldBeDifferent() {
        assertEquals(loanCalculator.calculateAmortizationSchedule(spec).size(),
                loanCalculator
                        .calculateAmortizationSchedule(spec)
                        .stream()
                        .map(MonthlyPayment::getInterestPayment)
                        .distinct()
                        .count());
    }

    @Test
    void allRemainingAmountsShouldBeDifferent() {
        assertEquals(loanCalculator.calculateAmortizationSchedule(spec).size(),
                loanCalculator
                        .calculateAmortizationSchedule(spec)
                        .stream()
                        .map(MonthlyPayment::getRemainingLoanAmount)
                        .distinct()
                        .count());
    }
}
