package com.pismo.code.assessment.domain.dto.account;

import java.util.UUID;

public class AccountCreationDtoMock {

    public static AccountCreationDto generateInstance(Long documentNumber, UUID accountId) {
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber(documentNumber);
        accountCreationDto.setAccountId(accountId);

        return accountCreationDto;
    }
}
