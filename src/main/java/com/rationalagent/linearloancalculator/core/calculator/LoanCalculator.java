package com.rationalagent.linearloancalculator.core.calculator;


import com.rationalagent.linearloancalculator.core.dto.LoanDetails;
import com.rationalagent.linearloancalculator.core.dto.LoanSummary;
import com.rationalagent.linearloancalculator.core.model.MonthlyPayment;
import com.rationalagent.linearloancalculator.core.model.Loan;
import com.rationalagent.linearloancalculator.core.util.LoanDetailsUtil;
import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;


public final class LoanCalculator {

    public static Loan calculateLoan(LoanDetails loanDetails) {
        var loan = LoanDetailsUtil.getLoan(loanDetails);
        var paymentSchedule = AmortizationCalculator.calculatePaymentSchedule(loanDetails);
        var summary = getLoanSummary(paymentSchedule);

        return buildLoan(loan, summary, paymentSchedule);
    }

    private static LoanSummary getLoanSummary(List<MonthlyPayment> paymentSchedule) {
        var paidPrincipal = paymentSchedule.stream().map(MonthlyPayment::getPrincipalPayment).reduce(ZERO, BigDecimal::add);
        var paidInterest = paymentSchedule.stream().map(MonthlyPayment::getInterestPayment).reduce(ZERO, BigDecimal::add);

        return new LoanSummary(paidPrincipal, paidInterest);
    }

    private static Loan buildLoan(Loan loan, LoanSummary loanSummary, List<MonthlyPayment> monthlyPayments) {
        loan.setLoanSummary(loanSummary);
        loan.setPaymentSchedule(monthlyPayments);

        return loan;
    }

}
