package com.rationalagent.linearloancalculator.api.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String createLoanJSON;
    private static String updateLoanJSON;
    private static String invalidUpdateLoanJSON;


    @BeforeAll
    public static void loadFiles() throws IOException {
        createLoanJSON = Files.readString(Path.of("src/test/resources/payloads/create-loan.json"));
        updateLoanJSON = Files.readString(Path.of("src/test/resources/payloads/update-loan.json"));
        invalidUpdateLoanJSON = Files.readString(Path.of("src/test/resources/payloads/invalid-update-loan.json"));
    }

    @Test
    void givenARunningApplication_whenRequestingAllLoans_thenReturns200() throws Exception {
        mockMvc.perform(get("/loans")).andExpect(status().isOk());
    }

    @Test
    void givenARunningApplication_whenSendingRequestWithValidPayload_thenReturns200() throws Exception {
        mockMvc.perform(
                        post("/loans")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createLoanJSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenARunningApplication_whenCalculatingNewPaymentSchedule_thenReturns200() throws Exception {
        mockMvc.perform(
                post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createLoanJSON));

        mockMvc.perform(
                        post("/loans/1/payment-schedule/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateLoanJSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUsingInvalidUpdateJSON() throws Exception {
        mockMvc.perform(
                post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createLoanJSON));

        mockMvc.perform(
                        put("/loans/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidUpdateLoanJSON))
                .andExpect(status().isBadRequest());
    }
}
