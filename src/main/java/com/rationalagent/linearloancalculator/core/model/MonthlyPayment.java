package com.rationalagent.linearloancalculator.core.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MonthlyPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal principalPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingLoanAmount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Loan loan;

}