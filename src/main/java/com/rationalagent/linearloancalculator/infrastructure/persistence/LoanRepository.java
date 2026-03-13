package com.rationalagent.linearloancalculator.infrastructure.persistence;

import com.rationalagent.linearloancalculator.core.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Transactional
    default Loan upsert(Loan loan) {
        return save(loan);
    }

}
