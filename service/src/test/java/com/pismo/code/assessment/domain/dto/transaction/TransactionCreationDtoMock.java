package com.pismo.code.assessment.domain.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionCreationDtoMock {

    public static TransactionCreationDto generateInstance(UUID accountId, UUID operationType, BigDecimal amount) {
        TransactionCreationDto transactionCreationDto = new TransactionCreationDto();
        transactionCreationDto.setOperationType(operationType);
        transactionCreationDto.setAmount(amount);
        transactionCreationDto.setAccountId(accountId);

        return transactionCreationDto;
    }
}
