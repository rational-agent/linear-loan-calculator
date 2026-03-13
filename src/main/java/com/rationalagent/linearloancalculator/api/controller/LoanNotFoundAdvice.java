package com.rationalagent.linearloancalculator.api.controller;

import com.rationalagent.linearloancalculator.api.exception.LoanDetailsNotPresent;
import com.rationalagent.linearloancalculator.api.exception.LoanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class LoanNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String loanNotFoundHandler(LoanNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(LoanDetailsNotPresent.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String loanSpecificationNotPresentHandler(LoanDetailsNotPresent ex) {
        return ex.getMessage();
    }
}