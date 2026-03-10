package com.rationalagent.loancalculator.api.controller;

import com.rationalagent.loancalculator.core.model.Loan;
import com.rationalagent.loancalculator.core.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final LoanService service;

    @PostMapping("/loans/{id}/payment-schedule/calculate")
    public Loan calculate(@PathVariable Long id, @RequestBody @Valid Loan loan) {
        return service.calculate(id, loan);
    }
}
