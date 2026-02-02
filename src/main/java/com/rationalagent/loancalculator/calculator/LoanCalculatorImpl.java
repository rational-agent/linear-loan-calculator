package com.rationalagent.loancalculator.calculator;

import com.rationalagent.loancalculator.loan.repository.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

import static com.rationalagent.loancalculator.calculator.LoanCalculatorHelper.getRoundedAmount;

public class LoanCalculatorImpl implements LoanCalculator {

    /**
     * The unpaid portion of the loan amount. The principal balance does not include interest or any other charges.
     */
    BinaryOperator<BigDecimal> recalculatePrincipalBalance = (balance, payment) -> {
        if (balance.compareTo(payment) < 0) {
            return payment;
        } else {
            return balance.subtract(payment);
        }
    };


    public Loan calculateLoan(LoanSpecification spec) {
        Integer termAmount = calculateTermInMonths(spec);
        BigDecimal interestRateAsDecimal = spec.getInterestRate().movePointLeft(2);

        Payment regular = calculatePrincipalPayment(spec);
        Payment firstMonth = calculateFirstPrincipalPayment(spec);

        LocalDate paymentDate = spec.getStartDate().withDayOfMonth(spec.getPayDay());

        BigDecimal principalBalance = spec.getPrincipal();
        BigDecimal principalBalanceError = regular.getPaymentRoundingError().multiply(new BigDecimal(termAmount));

        BigDecimal payedLoanAmount = BigDecimal.ZERO;
        BigDecimal payedInterestAmount = BigDecimal.ZERO;
        BigDecimal totalPrincipalError = BigDecimal.ZERO;
        BigDecimal totalInterestError = BigDecimal.ZERO;

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

            BigDecimal payedInterest = calculateMonthlyInterest(principalBalance, interestRateAsDecimal);
            BigDecimal payedInterestOnError = calculateMonthlyInterest(principalBalanceError, interestRateAsDecimal);

            principalBalance = recalculatePrincipalBalance.apply(principalBalance, principalPayment);

            paymentDate = paymentDate.plusMonths(1);

            principalBalanceError = principalBalanceError.subtract(regular.getPaymentRoundingError());

            payedLoanAmount = payedLoanAmount.add(principalPayment);
            payedInterestAmount = payedInterestAmount.add(payedInterest);
            totalPrincipalError = totalPrincipalError.add(regular.getPaymentRoundingError());
            totalInterestError = totalInterestError.add(payedInterestOnError);
        }

        return buildLoan(spec, payedLoanAmount, payedInterestAmount, totalPrincipalError, totalInterestError, calculateAmortizationSchedule(spec));
    }

    @Override
    public Payment calculateFirstPrincipalPayment(LoanSpecification spec) {
        BigDecimal startDateWeight = LoanCalculatorHelper.getMonthWeight(spec.getStartDate());
        Payment regularPayment = calculatePrincipalPayment(spec);
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, regularPayment.getPaymentUnrounded().multiply(startDateWeight));
    }

    public Payment calculatePrincipalPayment(LoanSpecification spec) {
        return new Payment(PaymentType.PRINCIPAL_PAYMENT, spec.getPrincipal().divide(new BigDecimal(calculateTermInMonths(spec)), 10, RoundingMode.HALF_EVEN));
    }

    @Override
    public List<MonthlyPayment> calculateAmortizationSchedule(LoanSpecification spec) {
        BigDecimal interestRateAsDecimal = spec.getInterestRate().movePointLeft(2);

        Payment regular = calculatePrincipalPayment(spec);
        Payment firstMonth = calculateFirstPrincipalPayment(spec);

        LocalDate paymentDate = spec.getStartDate().withDayOfMonth(spec.getPayDay());

        BigDecimal principalBalance = spec.getPrincipal();

        BigDecimal totalPrincipalError = BigDecimal.ZERO;
        List<MonthlyPayment> monthlyPayments = new ArrayList<>();

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

            Payment interestPayment = new Payment(PaymentType.INTEREST_PAYMENT, calculateMonthlyInterest(principalBalance, interestRateAsDecimal));

            principalBalance = recalculatePrincipalBalance.apply(principalBalance, principalPayment.getPaymentRounded());
            monthlyPayments.add(new MonthlyPayment(
                    paymentDate,
                    principalPayment.getPaymentUnrounded(),
                    interestPayment.getPaymentUnrounded(),
                    principalBalance));
            paymentDate = paymentDate.plusMonths(1);

        }

        return monthlyPayments;
    }

    public Integer calculateTermInMonths(LoanSpecification spec) {
        return LoanCalculatorHelper.calculateLoanLifeInMonths(spec.getStartDate(), spec.getEndDate());
    }

    @Override
    public BigDecimal calculateMonthlyInterestRate(LoanSpecification spec) {
        return spec.getInterestRate().divide(MONTHS_IN_A_YEAR, 10, RoundingMode.HALF_EVEN);
    }

    private Loan buildLoan(LoanSpecification loanSpecification, BigDecimal payedLoanAmount, BigDecimal totalInterestPayed, BigDecimal totalPrincipalError, BigDecimal totalInterestError, List<MonthlyPayment> monthlyPayments) {
        var loan = new Loan(
                getRoundedAmount(loanSpecification.getPrincipal()),
                loanSpecification.getInterestRate(),
                loanSpecification.getStartDate(),
                loanSpecification.getEndDate(),
                loanSpecification.getPayDay(),
                loanSpecification.getAmortizationMethod()
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

    private BigDecimal calculateMonthlyInterest(BigDecimal remainingPrincipal, BigDecimal decimalInterest) {
        return remainingPrincipal.multiply(LoanCalculatorHelper.calculateMonthlyInterestRate(decimalInterest));
    }

}
