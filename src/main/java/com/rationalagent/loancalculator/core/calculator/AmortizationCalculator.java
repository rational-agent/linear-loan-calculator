package com.rationalagent.loancalculator.core.calculator;

import com.rationalagent.loancalculator.core.dto.LoanDetails;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import com.rationalagent.loancalculator.core.calculator.dto.Payment;
import com.rationalagent.loancalculator.core.calculator.dto.PaymentType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.rationalagent.loancalculator.core.calculator.LoanCalculatorHelper.recalculatePrincipalBalance;

/**
 * A utility class for converting lengths between metric and imperial units.
 *
 */
public final class AmortizationCalculator {

    private AmortizationCalculator() {

    }

    /**
     * Calculates the amortization schedule for a loan.
     *
     * @return a list of monthly payments, also known as the amortization schedule.
     */
    public static List<MonthlyPayment> calculateAmortizationSchedule(LoanDetails spec) {
        var interestRateAsDecimal = spec.interestRate().movePointLeft(2);

        var firstMonth = LoanCalculatorHelper.calculateFirstPrincipalPayment(spec);
        var regular = LoanCalculatorHelper.calculatePrincipalPayment(spec);
        var paymentDate = spec.startDate().withDayOfMonth(spec.payDay());

        var principalBalance = spec.principal();
        var monthlyPayments = new ArrayList<MonthlyPayment>();
        while (principalBalance.compareTo(BigDecimal.ZERO) > 0) {
            var principalPayment = getPrincipalPayment(spec, principalBalance, firstMonth, regular);
            var interestPayment = getInterestPayment(principalBalance, interestRateAsDecimal);

            principalBalance = recalculatePrincipalBalance(principalBalance, principalPayment.getPaymentRounded());

            monthlyPayments.add(
                    new MonthlyPayment(
                            paymentDate,
                            principalPayment.getPaymentRounded(),
                            interestPayment.getPaymentRounded(),
                            principalBalance)
            );

            paymentDate = paymentDate.plusMonths(1);
        }

        return monthlyPayments;
    }

    private static Payment getInterestPayment(BigDecimal principalBalance, BigDecimal interestRateAsDecimal) {
        var monthlyInterest = LoanCalculatorHelper.calculateMonthlyInterest(principalBalance, interestRateAsDecimal);
        return new Payment(PaymentType.INTEREST_PAYMENT, monthlyInterest);
    }

    private static Payment getPrincipalPayment(
            LoanDetails spec, BigDecimal principalBalance, Payment firstMonth, Payment regular) {
        if (principalBalance.compareTo(spec.principal()) == 0) {
            return firstMonth;
        } else if (principalBalance.compareTo(regular.getPaymentRounded()) <= 0) {
            return new Payment(PaymentType.PRINCIPAL_PAYMENT, principalBalance);
        } else {
            return regular;
        }
    }
}
