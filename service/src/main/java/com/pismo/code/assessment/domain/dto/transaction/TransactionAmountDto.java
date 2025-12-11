package com.pismo.code.assessment.domain.dto.transaction;

import com.pismo.code.assessment.domain.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAmountDto {

    private UUID transactionId;
    private UUID operationTypeId;
    private BigDecimal balance;
}
