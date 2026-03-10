package com.rationalagent.loancalculator.core.calculator;

import com.rationalagent.loancalculator.core.dto.Payment;
import com.rationalagent.loancalculator.core.dto.PaymentType;
import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.rationalagent.loancalculator.core.calculator.LoanCalculatorHelper.recalculatePrincipalBalance;
import static java.math.BigDecimal.ZERO;

@NoArgsConstructor
public final class AmortizationCalculator {

    public static List<MonthlyPayment> calculateAmortizationSchedule(LoanDetails loanDetails) {

        var firstPrincipalPayment = LoanCalculatorHelper.calculateFirstPrincipalPayment(loanDetails);
        var regularPrincipalPayment = LoanCalculatorHelper.calculatePrincipalPayment(loanDetails);
        var paymentDate = loanDetails.startDate().withDayOfMonth(loanDetails.payDay());

        var principalBalance = loanDetails.principal();
        var monthlyPayments = new ArrayList<MonthlyPayment>();

        final var interestRateAsDecimal = loanDetails.interestRate().movePointLeft(2);
        while (loanNotFullyRepaid(principalBalance)) {
            final var principalPayment = getPrincipalPayment(loanDetails, principalBalance, firstPrincipalPayment, regularPrincipalPayment);
            final var interestPayment = getInterestPayment(principalBalance, interestRateAsDecimal);

            principalBalance = recalculatePrincipalBalance(principalBalance, principalPayment.getPaymentRounded());
            var monthlyPayment = new MonthlyPayment(
                    paymentDate,
                    principalPayment.getPaymentRounded(),
                    interestPayment.getPaymentRounded(),
                    principalBalance
            );

            monthlyPayments.add(monthlyPayment);
            paymentDate = paymentDate.plusMonths(1);
        }

        return monthlyPayments;
    }

    private static boolean loanNotFullyRepaid(BigDecimal principalBalance) {
        return principalBalance.compareTo(ZERO) > 0;
    }

    private static Payment getInterestPayment(BigDecimal principalBalance, BigDecimal interestRateAsDecimal) {
        var monthlyInterest = LoanCalculatorHelper.calculateMonthlyInterest(principalBalance, interestRateAsDecimal);
        return new Payment(PaymentType.INTEREST_PAYMENT, monthlyInterest);
    }

    private static Payment getPrincipalPayment(LoanDetails loanDetails,
                                               BigDecimal principalBalance,
                                               Payment firstPrincipalPayment,
                                               Payment regularPrincipalPayment) {
        if (isFirstPrincipalPayment(loanDetails, principalBalance)) {
            return firstPrincipalPayment;
        } else if (isRegularPrincipalPayment(principalBalance, regularPrincipalPayment)) {
            return new Payment(PaymentType.PRINCIPAL_PAYMENT, principalBalance);
        } else {
            return regularPrincipalPayment;
        }
    }

    private static boolean isFirstPrincipalPayment(LoanDetails loanDetails, BigDecimal principalBalance) {
        return principalBalance.compareTo(loanDetails.principal()) == 0;
    }

    private static boolean isRegularPrincipalPayment(BigDecimal principalBalance, Payment regularPrincipalPayment) {
        return principalBalance.compareTo(regularPrincipalPayment.getPaymentRounded()) <= 0;
    }
}
