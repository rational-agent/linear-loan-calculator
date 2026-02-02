package com.rationalagent.loancalculator.loan.repository;

import com.rationalagent.loancalculator.loan.repository.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Transactional
    default Loan upsert(Loan entity) {
        return save(entity);
    }

}
