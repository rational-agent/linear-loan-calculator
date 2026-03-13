package com.rationalagent.linearloancalculator.core.calculator;


import com.rationalagent.linearloancalculator.core.dto.LoanDetails;
import com.rationalagent.linearloancalculator.core.model.Loan;
import com.rationalagent.linearloancalculator.core.model.MonthlyPayment;
import com.rationalagent.linearloancalculator.core.util.LoanDetailsUtil;
import java.util.List;


public final class LoanCalculator {

    public static Loan calculateLoan(LoanDetails loanDetails) {
        var loan = LoanDetailsUtil.getLoan(loanDetails);
        var paymentSchedule = AmortizationCalculator.calculatePaymentSchedule(loanDetails);

        return buildLoan(loan, paymentSchedule);
    }

    private static Loan buildLoan(Loan loan, List<MonthlyPayment> monthlyPayments) {
        loan.setPaymentSchedule(monthlyPayments);

        return loan;
    }

}
