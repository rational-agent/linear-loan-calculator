package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.LoanSpecification;
import com.rationalagent.loancalculator.loan.repository.model.MonthlyPayment;
import com.rationalagent.loancalculator.loan.repository.model.Payment;
import com.rationalagent.loancalculator.loan.repository.model.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AmortizationCalculator {

    public static List<MonthlyPayment> calculateAmortizationSchedule(LoanSpecification spec) {
        var interestRateAsDecimal = spec.getInterestRate().movePointLeft(2);

        var regular = LoanCalculatorHelper.calculatePrincipalPayment(spec);
        var firstMonth = LoanCalculatorHelper.calculateFirstPrincipalPayment(spec);

        var paymentDate = spec.getStartDate().withDayOfMonth(spec.getPayDay());

        var principalBalance = spec.getPrincipal();

        var totalPrincipalError = BigDecimal.ZERO;
        var monthlyPayments = new ArrayList<MonthlyPayment>();

        while (principalBalance.compareTo(BigDecimal.ZERO) > 0) {

            Payment principalPayment;
            if (principalBalance.compareTo(spec.getPrincipal()) == 0) {
                principalPayment = firstMonth;
                totalPrincipalError = totalPrincipalError.add(firstMonth.getPaymentRoundingError());
            } else if (principalBalance.compareTo(regular.getPaymentRounded()) <= 0) {
                principalPayment = new Payment(PaymentType.PRINCIPAL_PAYMENT, principalBalance);
                totalPrincipalError = totalPrincipalError.add(principalPayment.getPaymentRoundingError());
            } else {
                principalPayment = regular;
                totalPrincipalError = totalPrincipalError.add(regular.getPaymentRoundingError());
            }

            var interestPayment = new Payment(PaymentType.INTEREST_PAYMENT, LoanCalculatorHelper.calculateMonthlyInterest(principalBalance, interestRateAsDecimal));

            principalBalance = LoanCalculatorHelper.recalculatePrincipalBalance(principalBalance, principalPayment.getPaymentRounded());
            monthlyPayments.add(new MonthlyPayment(
                    paymentDate,
                    principalPayment.getPaymentUnrounded(),
                    interestPayment.getPaymentUnrounded(),
                    principalBalance));
            paymentDate = paymentDate.plusMonths(1);

        }

        return monthlyPayments;
    }
}
