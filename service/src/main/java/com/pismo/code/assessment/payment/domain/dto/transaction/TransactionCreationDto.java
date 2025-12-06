package com.pismo.code.assessment.payment.domain.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TransactionCreationDto {

    @JsonIgnore
    private UUID transactionId = UUID.randomUUID();
    private UUID accountId;
    private UUID operationType;
    private BigDecimal amount;
}
