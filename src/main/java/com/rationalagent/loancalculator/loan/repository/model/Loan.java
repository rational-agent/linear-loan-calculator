package com.rationalagent.loancalculator.loan.repository.model;



import jakarta.persistence.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{error.principal.mandatory}")
    @Positive(message = "{error.principal.belowMinimum}")
    @Max(value = 1_000_000_000, message = "{error.principal.aboveMaximum}")
    private BigDecimal principal;

    @NotNull(message = "{error.interest.mandatory}")
    @Min(value = 0, message = "{error.interest.belowMinimum}")
    @Max(value = 100, message = "{error.interest.aboveMaximum}")
    private BigDecimal interestRate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Min(value = 1, message = "{error.interest.belowMinimum}")
    @Max(value = 28, message = "{error.interest.aboveMaximum}")
    private int payDay;

    @NotNull(message = "{error.amortizationMethod.mandatory}")
    private AmortizationMethod amortizationMethod;

    @Transient
    private AmortizationSummary amortizationSummary;

    @Transient
    private List<MonthlyPayment> amortizationSchedule;

    public Loan() {
    }

    public Loan(BigDecimal principal, BigDecimal interestRate, LocalDate startDate, LocalDate endDate, int payDay) {
        this.principal = principal;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payDay = payDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPayDay() {
        return payDay;
    }

    public void setPayDay(int payDay) {
        this.payDay = payDay;
    }

    public AmortizationMethod getAmortizationMethod() {
        return amortizationMethod;
    }

    public void setAmortizationMethod(AmortizationMethod amortizationMethod) {
        this.amortizationMethod = amortizationMethod;
    }

    public AmortizationSummary getAmortizationSummary() {
        return amortizationSummary;
    }

    public void setAmortizationSummary(AmortizationSummary amortizationSummary) {
        this.amortizationSummary = amortizationSummary;
    }

    public List<MonthlyPayment> getAmortizationSchedule() {
        return amortizationSchedule;
    }

    public void setAmortizationSchedule(List<MonthlyPayment> amortizationSchedule) {
        this.amortizationSchedule = amortizationSchedule;
    }
}