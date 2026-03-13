package com.rationalagent.linearloancalculator.core.service;

import com.rationalagent.linearloancalculator.api.exception.LoanNotFoundException;
import com.rationalagent.linearloancalculator.core.calculator.LoanCalculator;
import com.rationalagent.linearloancalculator.core.model.Loan;
import com.rationalagent.linearloancalculator.core.util.LoanDetailsUtil;
import com.rationalagent.linearloancalculator.infrastructure.persistence.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public List<Loan> readAll() {
        return loanRepository.findAll();
    }

    public Loan calculate(Long id, Loan loan) {
        var loanDetails = LoanDetailsUtil.getLoanDetails(loan);
        var calculatedLoan = LoanCalculator.calculateLoan(loanDetails);

        return update(id, calculatedLoan);
    }

    public Loan create(Loan loan) {
        return loanRepository.upsert(loan);
    }

    public Loan update(Long id, Loan loanUpdate) {
        return loanRepository.findById(id)
                .map(loan -> {
                    loanRepository.deletePaymentSchedule(loan);
                    applyUpdate(loan, loanUpdate);
                    return loanRepository.upsert(loan);
                })
                .orElseThrow(() -> new LoanNotFoundException("Loan with id not found: " + id));
    }

    public Loan read(Long id) {
        return loanRepository.getReferenceById(id);
    }

    public boolean delete(Long id) {
        loanRepository.deleteById(id);
        return true;
    }

    private static void applyUpdate(Loan loan, Loan loanUpdate) {
        loan.setPrincipal(loanUpdate.getPrincipal());
        loan.setInterestRate(loanUpdate.getInterestRate());
        loan.setStartDate(loanUpdate.getStartDate());
        loan.setEndDate(loanUpdate.getEndDate());
        loan.setPayDay(loanUpdate.getPayDay());

        loan.setPaymentSchedule(loanUpdate.getPaymentSchedule());
        loan.setLoanSummary(loanUpdate.getLoanSummary());
    }
}
