package com.pismo.code.assessment.payment.domain.dto.account;

import com.pismo.code.assessment.payment.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountDto {
    private UUID accountId;
    private Long documentNumber;

    public static AccountDto mapEntity(Account account) {
        return new AccountDto(account.getAccountId(), account.getDocumentNumber());
    }
}
