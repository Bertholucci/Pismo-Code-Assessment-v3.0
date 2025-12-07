package com.pismo.code.assessment.controller;

import com.pismo.code.assessment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionCreationDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionDto;
import com.pismo.code.assessment.helper.ApiConstants;
import com.pismo.code.assessment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
@Tag(name = "Transaction", description = "Operations related to monetary transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Create transaction",
            description = "Creates a new monetary transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto createTransaction(@RequestBody TransactionCreationDto accountCreation) {
        return transactionService.persist(accountCreation);
    }

    @Operation(
            summary = "Get transaction ID",
            description = "Fetches specific transaction by the identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @GetMapping
    public TransactionDto searchTransaction(@RequestParam(name = ApiConstants.TRANSACTION_ID) UUID transactionId) {
        return transactionService.searchTransaction(transactionId);
    }
}
