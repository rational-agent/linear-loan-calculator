package com.rationalagent.loancalculator.loan;

import com.rationalagent.loancalculator.calculator.LoanCalculator;
import com.rationalagent.loancalculator.loan.exceptions.LoanNotFoundException;
import com.rationalagent.loancalculator.loan.repository.LoanRepository;
import com.rationalagent.loancalculator.loan.repository.model.Loan;
import com.rationalagent.loancalculator.loan.repository.dto.LoanDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository repository;

    public List<Loan> calculateAll() {
        return readAll().stream()
                .map(loan -> calculate(loan.getId(), loan))
                .toList();
    }

    public Loan calculate(Long id, Loan loan) {
        var spec = getLoanDetails(loan);
        var calculatedLoan = LoanCalculator.calculateLoan(spec);

        return update(id, calculatedLoan);
    }

    public Loan create(Loan loan) {
        return repository.upsert(loan);
    }

    public Loan update(Long id, Loan loanUpdate) {
        return repository.findById(id)
                .map(loan -> {
                    applyUpdate(loan, loanUpdate);
                    return repository.upsert(loan);
                })
                .orElseThrow(() -> new LoanNotFoundException("Loan with id not found: " + id));
    }

    public List<Loan> readAll() {
        return repository.findAll();
    }

    public Loan read(Long id) {
        return repository.getReferenceById(id);
    }

    public boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    private static LoanDetails getLoanDetails(Loan loan) {
        return new LoanDetails(
                loan.getPrincipal(),
                loan.getInterestRate(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getPayDay()
        );
    }

    private static void applyUpdate(Loan loan, Loan loanUpdate) {
        loan.setPrincipal(loanUpdate.getPrincipal());
        loan.setInterestRate(loanUpdate.getInterestRate());
        loan.setStartDate(loanUpdate.getStartDate());
        loan.setEndDate(loanUpdate.getEndDate());
        loan.setPayDay(loanUpdate.getPayDay());

        loan.setAmortizationSchedule(loanUpdate.getAmortizationSchedule());
        loan.setAmortizationSummary(loanUpdate.getAmortizationSummary());
    }
}
