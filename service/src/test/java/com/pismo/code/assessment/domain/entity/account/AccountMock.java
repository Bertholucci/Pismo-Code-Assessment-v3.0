package com.pismo.code.assessment.domain.entity.account;

import com.pismo.code.assessment.domain.entity.Account;

import java.util.UUID;

public class AccountMock {

    public static Account generateInstance(Long documentNumber, UUID accountId) {
        return new Account(accountId, documentNumber);
    }
}
