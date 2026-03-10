package com.rationalagent.loancalculator.core.model;

import com.rationalagent.loancalculator.core.dto.LoanSummary;
import com.rationalagent.loancalculator.core.dto.MonthlyPayment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
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

    @Transient
    private LoanSummary loanSummary;

    @Transient
    private List<MonthlyPayment> paymentSchedule;

    public Loan(BigDecimal principal, BigDecimal interestRate, LocalDate startDate, LocalDate endDate, int payDay) {
        this.principal = principal;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payDay = payDay;
    }

}