package com.rationalagent.loancalculator.loan;

import com.rationalagent.loancalculator.calculator.LoanCalculator;
import com.rationalagent.loancalculator.loan.exceptions.LoanNotFoundException;
import com.rationalagent.loancalculator.loan.repository.LoanRepository;
import com.rationalagent.loancalculator.loan.repository.model.Loan;
import com.rationalagent.loancalculator.loan.repository.model.LoanSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    LoanRepository repository;

    public Loan calculate(Long id, Loan loan) {
        var spec = getLoanSpecification(loan);
        var calculatedLoan = LoanCalculator.calculateLoan(spec);

        return update(id, calculatedLoan);
    }

    public Loan create(Loan loan) {
        return repository.upsert(loan);
    }

    public Loan update(Long id, Loan loanUpdate) {
        return repository.findById(id)
                .map(loan -> {
                    loan.setPrincipal(loanUpdate.getPrincipal());
                    loan.setInterestRate(loanUpdate.getInterestRate());
                    loan.setStartDate(loanUpdate.getStartDate());
                    loan.setEndDate(loanUpdate.getEndDate());
                    loan.setPayDay(loanUpdate.getPayDay());
                    loan.setAmortizationMethod(loanUpdate.getAmortizationMethod());

                    loan.setAmortizationSchedule(loanUpdate.getAmortizationSchedule());
                    loan.setAmortizationSummary(loanUpdate.getAmortizationSummary());
                    return repository.upsert(loan);
                })
                .orElseThrow(() -> new LoanNotFoundException("Loan with id not found: " + id));
    }

    public List<Loan> readAll() {
        return repository.findAll();
    }

    public Loan read(Long id) {
        return repository.getOne(id);
    }

    public boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    private static LoanSpecification getLoanSpecification(Loan loan) {
        return new LoanSpecification(
                loan.getPrincipal(),
                loan.getInterestRate(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getPayDay()
        );
    }
}
