package com.pismo.code.assessment.domain.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class TransactionCreationDto {

    @JsonIgnore
    private UUID transactionId = UUID.randomUUID();
    private UUID accountId;
    private UUID operationType;
    private BigDecimal amount;
}
