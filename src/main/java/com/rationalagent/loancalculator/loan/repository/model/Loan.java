package com.rationalagent.loancalculator.loan.repository.model;



import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanSpecification loanSpecification;

    @OneToOne(cascade = CascadeType.ALL)
    private AmortizationSummary amortizationSummary;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MonthlyPayment> amortizationSchedule;

    public Loan() {
    }

    public Loan(LoanSpecification loanSpecification, AmortizationSummary amortizationSummary, List<MonthlyPayment> amortizationSchedule) {
        this.loanSpecification = loanSpecification;
        this.amortizationSummary = amortizationSummary;
        this.amortizationSchedule = amortizationSchedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanSpecification getLoanSpecification() {
        return loanSpecification;
    }

    public void setLoanSpecification(LoanSpecification loanSpecification) {
        this.loanSpecification = loanSpecification;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id) && Objects.equals(loanSpecification, loan.loanSpecification) && Objects.equals(amortizationSummary, loan.amortizationSummary) && Objects.equals(amortizationSchedule, loan.amortizationSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loanSpecification, amortizationSummary, amortizationSchedule);
    }
}