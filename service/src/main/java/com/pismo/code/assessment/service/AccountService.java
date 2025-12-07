package com.pismo.code.assessment.service;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDto;
import com.pismo.code.assessment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDto persist(AccountCreationDto accountCreationRequest) {
        if(this.findAccountByDocumentNumber(accountCreationRequest.getDocumentNumber()).isPresent()) {
            throw new BadRequestException("The resource can not be created, because the document number is already registered for another user");
        }

        try {
            Account savedAccount = this.save(mapAccountForSaveOperation(accountCreationRequest));
            return AccountDto.mapEntity(savedAccount);
        } catch (Exception exception) {
            log.error("There was an error trying to persist the account {} with document number {}, because of {}", accountCreationRequest.getAccountId(), accountCreationRequest.getDocumentNumber(), exception.getLocalizedMessage());
            throw new RuntimeException(String.format("There was an error trying to persist the account %s. Please contact the system administrator if the error continues", accountCreationRequest.getAccountId()));
        }
    }

    public AccountDto searchAccount(UUID accountId) {
        Optional<Account> account = this.findById(accountId);

        if(account.isEmpty()) {
            throw new BadRequestException(String.format("The informed account '%s' does not exist", accountId));
        }

        return AccountDto.mapEntity(account.get());
    }

    public Boolean isAccountValid(UUID accountId) {
        return this.findById(accountId).isPresent();
    }

    private Account save(Account account) {
        return accountRepository.save(account);
    }

    private Optional<Account> findById(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    private Optional<Account> findAccountByDocumentNumber(Long documentNumber) {
        return accountRepository.findByDocumentNumber(documentNumber);
    }

    private Account mapAccountForSaveOperation(AccountCreationDto accountCreationRequest) {
        return Account.builder()
            .accountId(accountCreationRequest.getAccountId())
            .documentNumber(accountCreationRequest.getDocumentNumber())
            .build();
    }
}
