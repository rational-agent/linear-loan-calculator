package com.rationalagent.linearloancalculator.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        HttpStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") LocalDateTime timestamp,
        String message,
        List<String> subErrors) {
}
