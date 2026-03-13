package com.rationalagent.linearloancalculator.core.util;

import com.rationalagent.linearloancalculator.core.dto.LoanDetails;
import com.rationalagent.linearloancalculator.core.model.Loan;

public class LoanDetailsUtil {

    public static LoanDetails getLoanDetails(Loan loan) {
        return new LoanDetails(
                loan.getPrincipal(),
                loan.getInterestRate(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getPayDay()
        );
    }

    public static Loan getLoan(LoanDetails loanDetails) {
        return new Loan(
                loanDetails.principal(),
                loanDetails.interestRate(),
                loanDetails.startDate(),
                loanDetails.endDate(),
                loanDetails.payDay()
        );
    }

}
