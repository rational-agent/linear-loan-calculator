package com.rationalagent.linearloancalculator.core.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "{error.belowMinimum}")
    @Max(value = 1_000_000_000, message = "{error.aboveMaximum}")
    private BigDecimal principal;

    @NotNull
    @Min(value = 0, message = "{error.belowMinimum}")
    @Max(value = 100, message = "{error.aboveMaximum}")
    private BigDecimal interestRate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Min(value = 1, message = "{error.interest.belowMinimum}")
    @Max(value = 28, message = "{error.interest.aboveMaximum}")
    private Integer payDay;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MonthlyPayment> paymentSchedule = new ArrayList<>();

    public Loan(BigDecimal principal, BigDecimal interestRate, LocalDate startDate, LocalDate endDate, int payDay) {
        this.principal = principal;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payDay = payDay;
    }

}