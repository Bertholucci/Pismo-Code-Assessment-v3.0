package com.pismo.code.assessment.controller;

import com.pismo.code.assessment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeCreationDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeDto;
import com.pismo.code.assessment.helper.ApiConstants;
import com.pismo.code.assessment.service.OperationTypeService;
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
@RequestMapping("/v1/operation-type")
@Tag(name = "Operation Type", description = "Operations related to operations type")
public class OperationTypeController {

    private final OperationTypeService operationTypeService;

    @Operation(
        summary = "Create operation type",
        description = "Creates a new operation type using the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Operation type created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperationTypeDto createOperationType(@RequestBody OperationTypeCreationDto operationTypeCreation) {
        return operationTypeService.persist(operationTypeCreation);
    }

    @Operation(
        summary = "Get operation type by ID",
        description = "Fetches specific operation type by the identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation type fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationTypeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @GetMapping
    public OperationTypeDto searchOperationType(@RequestParam(name = ApiConstants.OPERATION_TYPE_ID) UUID operationTypeId) {
        return operationTypeService.searchOperationType(operationTypeId);
    }
}
