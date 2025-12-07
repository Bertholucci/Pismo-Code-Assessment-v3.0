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
public class TransactionDto {

    private UUID transactionId;
    private UUID accountId;
    private UUID operationTypeId;
    private BigDecimal amount;

    public static TransactionDto mapEntity(Transaction transaction) {
        return new TransactionDto(transaction.getTransactionId(), transaction.getAccountId(), transaction.getOperationTypeId(), transaction.getAmount());
    }
}
