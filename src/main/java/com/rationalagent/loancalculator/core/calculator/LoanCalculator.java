package com.rationalagent.loancalculator.core.calculator;


import com.rationalagent.loancalculator.core.dto.AmortizationSummary;
import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import com.rationalagent.loancalculator.core.model.Loan;
import java.math.BigDecimal;
import java.util.List;


public final class LoanCalculator {

    public static Loan calculateLoan(LoanDetails loanDetails) {
        var amortization = AmortizationCalculator.calculateAmortizationSchedule(loanDetails);

        var payedLoanAmount = amortization.stream()
                .map(MonthlyPayment::principalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var payedInterestAmount = amortization.stream()
                .map(MonthlyPayment::interestPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return buildLoan(loanDetails, payedLoanAmount, payedInterestAmount, amortization);
    }

    private static Loan buildLoan(LoanDetails loanDetails, BigDecimal paidPrincipal, BigDecimal paidInterest, List<MonthlyPayment> monthlyPayments) {
        var loan = new Loan(
                loanDetails.principal(),
                loanDetails.interestRate(),
                loanDetails.startDate(),
                loanDetails.endDate(),
                loanDetails.payDay()
        );
        var summary = new AmortizationSummary(
                paidPrincipal.add(paidInterest),
                paidPrincipal,
                paidInterest
        );

        loan.setAmortizationSummary(summary);
        loan.setAmortizationSchedule(monthlyPayments);

        return loan;
    }

}
