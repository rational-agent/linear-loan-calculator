package com.rationalagent.loancalculator.core.calculator;


import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.LoanSummary;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import com.rationalagent.loancalculator.core.model.Loan;
import com.rationalagent.loancalculator.core.util.LoanDetailsUtil;
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
        var paidPrincipal = paymentSchedule.stream().map(MonthlyPayment::principalPayment).reduce(ZERO, BigDecimal::add);
        var paidInterest = paymentSchedule.stream().map(MonthlyPayment::interestPayment).reduce(ZERO, BigDecimal::add);

        return new LoanSummary(paidPrincipal, paidInterest);
    }

    private static Loan buildLoan(Loan loan, LoanSummary loanSummary, List<MonthlyPayment> monthlyPayments) {
        loan.setLoanSummary(loanSummary);
        loan.setPaymentSchedule(monthlyPayments);

        return loan;
    }

}
