package com.pismo.code.assessment.payment.controller;

import com.pismo.code.assessment.payment.domain.dto.account.AccountCreationDto;
import com.pismo.code.assessment.payment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.payment.helper.ApiConstants;
import com.pismo.code.assessment.payment.service.AccountService;
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
@RequestMapping("/v1/account")
@Tag(name = "Account", description = "Operations related to account management")
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "Create account",
        description = "Creates a new account using the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody AccountCreationDto accountCreation) {
        return accountService.persist(accountCreation);
    }

    @Operation(
        summary = "Get account account by ID",
        description = "Fetches specific account by the identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @GetMapping
    public AccountDto searchAccount(@RequestParam(name = ApiConstants.ACCOUNT_ID) UUID accountId) {
        return accountService.searchAccount(accountId);
    }
}
