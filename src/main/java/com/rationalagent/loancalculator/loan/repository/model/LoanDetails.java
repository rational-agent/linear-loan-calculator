package com.rationalagent.loancalculator.loan.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class LoanDetails {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanDetails() {
    }

    public LoanDetails(BigDecimal loanAmount, BigDecimal annualPercentageRate, LocalDate startDate, LocalDate endDate, int payDay) {
        this.principal = loanAmount;
        this.interestRate = annualPercentageRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payDay = payDay;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPayDay(int payDay) {
        this.payDay = payDay;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getPayDay() {
        return payDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoanDetails that = (LoanDetails) o;
        return payDay == that.payDay && Objects.equals(principal, that.principal) && Objects.equals(interestRate, that.interestRate) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal, interestRate, startDate, endDate, payDay);
    }
}
