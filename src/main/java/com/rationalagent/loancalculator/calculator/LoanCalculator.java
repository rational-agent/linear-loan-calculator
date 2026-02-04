package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class LoanCalculator {

    public static Loan calculateLoan(LoanSpecification spec) {
        var termAmount = LoanCalculatorHelper.calculateTermInMonths(spec);
        var interestRateAsDecimal = spec.getInterestRate().movePointLeft(2);

        var regular = calculatePrincipalPayment(spec);
        var firstMonth = LoanCalculatorHelper.calculateFirstPrincipalPayment(spec);

        var paymentDate = spec.getStartDate().withDayOfMonth(spec.getPayDay());

        var principalBalance = spec.getPrincipal();
        var principalBalanceError = regular.getPaymentRoundingError().multiply(new BigDecimal(termAmount));

        var payedLoanAmount = BigDecimal.ZERO;
        var payedInterestAmount = BigDecimal.ZERO;
        var totalPrincipalError = BigDecimal.ZERO;
        var totalInterestError = BigDecimal.ZERO;

        while (principalBalance.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal principalPayment;
            if (principalBalance.compareTo(spec.getPrincipal()) == 0) {
                principalPayment = firstMonth.getPaymentRounded();
                totalPrincipalError = totalPrincipalError.add(firstMonth.getPaymentRoundingError());
            } else if (principalBalance.compareTo(regular.getPaymentRounded()) <= 0) {
                principalPayment = principalBalance;
                totalPrincipalError = totalPrincipalError.add(new Payment(PaymentType.PRINCIPAL_PAYMENT, principalPayment).getPaymentRoundingError());
            } else {
                principalPayment = regular.getPaymentRounded();
                totalPrincipalError = totalPrincipalError.add(regular.getPaymentRoundingError());
            }

            var payedInterest = calculateMonthlyInterest(principalBalance, interestRateAsDecimal);
            var payedInterestOnError = calculateMonthlyInterest(principalBalanceError, interestRateAsDecimal);

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

    private static Payment calculatePrincipalPayment(LoanSpecification spec) {
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, spec.getPrincipal().divide(new BigDecimal(LoanCalculatorHelper.calculateTermInMonths(spec)), 10, RoundingMode.HALF_EVEN));
    }

    private static Loan buildLoan(LoanSpecification loanSpecification, BigDecimal payedLoanAmount, BigDecimal totalInterestPayed, BigDecimal totalPrincipalError, BigDecimal totalInterestError, List<MonthlyPayment> monthlyPayments) {
        var loan = new Loan(
                getRoundedAmount(loanSpecification.getPrincipal()),
                loanSpecification.getInterestRate(),
                loanSpecification.getStartDate(),
                loanSpecification.getEndDate(),
                loanSpecification.getPayDay()
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
