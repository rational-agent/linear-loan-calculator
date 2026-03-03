package com.rationalagent.loancalculator.core.calculator;


import com.rationalagent.loancalculator.core.dto.AmortizationSummary;
import com.rationalagent.loancalculator.core.model.Loan;
import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import com.rationalagent.loancalculator.core.dto.Payment;
import com.rationalagent.loancalculator.core.dto.PaymentType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class LoanCalculator {

    public static Loan calculateLoan(LoanDetails spec) {
        var termAmount = LoanCalculatorHelper.calculateTermInMonths(spec);
        var interestRateAsDecimal = spec.interestRate().movePointLeft(2);

        var regular = calculatePrincipalPayment(spec);
        var firstMonth = LoanCalculatorHelper.calculateFirstPrincipalPayment(spec);

        var paymentDate = spec.startDate().withDayOfMonth(spec.payDay());

        var principalBalance = spec.principal();
        var principalBalanceError = regular.getPaymentRoundingError().multiply(new BigDecimal(termAmount));

        var payedLoanAmount = BigDecimal.ZERO;
        var payedInterestAmount = BigDecimal.ZERO;
        var totalPrincipalError = BigDecimal.ZERO;
        var totalInterestError = BigDecimal.ZERO;

        while (principalBalance.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal principalPayment;
            if (principalBalance.compareTo(spec.principal()) == 0) {
                principalPayment = firstMonth.getPaymentRounded();
                totalPrincipalError = totalPrincipalError.add(firstMonth.getPaymentRoundingError());
            } else if (principalBalance.compareTo(regular.getPaymentRounded()) <= 0) {
                principalPayment = principalBalance;
                totalPrincipalError = totalPrincipalError.add(new Payment(PaymentType.PRINCIPAL_PAYMENT, principalPayment).getPaymentRoundingError());
            } else {
                principalPayment = regular.getPaymentRounded();
                totalPrincipalError = totalPrincipalError.add(regular.getPaymentRoundingError());
            }

            final var payedInterest = calculateMonthlyInterest(principalBalance, interestRateAsDecimal);
            final var payedInterestOnError = calculateMonthlyInterest(principalBalanceError, interestRateAsDecimal);

            principalBalance = LoanCalculatorHelper.recalculatePrincipalBalance(principalBalance, principalPayment);

            paymentDate = paymentDate.plusMonths(1);

            principalBalanceError = principalBalanceError.subtract(regular.getPaymentRoundingError());

            payedLoanAmount = payedLoanAmount.add(principalPayment);
            payedInterestAmount = payedInterestAmount.add(payedInterest);
            totalPrincipalError = totalPrincipalError.add(regular.getPaymentRoundingError());
            totalInterestError = totalInterestError.add(payedInterestOnError);
        }

        var amortization = AmortizationCalculator.calculateAmortizationSchedule(spec);

        return buildLoan(spec, payedLoanAmount, payedInterestAmount, totalPrincipalError, totalInterestError, amortization);
    }

    private static Payment calculatePrincipalPayment(LoanDetails spec) {
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, spec.principal().divide(new BigDecimal(LoanCalculatorHelper.calculateTermInMonths(spec)), 10, RoundingMode.HALF_EVEN));
    }

    private static Loan buildLoan(LoanDetails loanDetails, BigDecimal payedLoanAmount, BigDecimal totalInterestPayed, BigDecimal totalPrincipalError, BigDecimal totalInterestError, List<MonthlyPayment> monthlyPayments) {
        var loan = new Loan(
                getRoundedAmount(loanDetails.principal()),
                loanDetails.interestRate(),
                loanDetails.startDate(),
                loanDetails.endDate(),
                loanDetails.payDay()
        );
        var summary = new AmortizationSummary(
                getRoundedAmount(payedLoanAmount).add(getRoundedAmount(totalInterestPayed)).add(getRoundedAmount(totalPrincipalError)).add(getRoundedAmount(totalInterestError)),
                getRoundedAmount(payedLoanAmount),
                getRoundedAmount(totalPrincipalError),
                getRoundedAmount(totalInterestPayed),
                getRoundedAmount(totalInterestError)
        );

        loan.setAmortizationSummary(summary);
        loan.setAmortizationSchedule(monthlyPayments);

        return loan;
    }

    private static BigDecimal calculateMonthlyInterest(BigDecimal remainingPrincipal, BigDecimal decimalInterest) {
        return remainingPrincipal.multiply(LoanCalculatorHelper.calculateMonthlyInterestRate(decimalInterest));
    }

    private static BigDecimal getRoundedAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
