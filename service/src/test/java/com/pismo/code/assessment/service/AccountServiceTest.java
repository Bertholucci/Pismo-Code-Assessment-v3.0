package com.pismo.code.assessment.service;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDtoMock;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.entity.account.AccountMock;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.exception.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    @InjectMocks
    private AccountService accountService;

    @MockitoBean
    private AccountRepository accountRepository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(AccountServiceTest.class);
    }

    @Test
    void shouldSucessfullyPersistANewAccountEntity() {
        Long documentNumber = 21390414L;
        UUID accountId = UUID.randomUUID();
        Account account = AccountMock.generateInstance(documentNumber, accountId);

        Mockito.when(accountRepository.findByDocumentNumber(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        accountService.persist(AccountCreationDtoMock.generateInstance(documentNumber, accountId));
    }

    @Test
    void shouldThrowErrorBecauseThereIsAlreadyAnAccountWithTheSpecifiedAccountNumber() {
        Long documentNumber = 21390414L;
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountRepository.findByDocumentNumber(Mockito.anyLong())).thenReturn(Optional.of(AccountMock.generateInstance(documentNumber, accountId)));

        try {
            accountService.persist(AccountCreationDtoMock.generateInstance(documentNumber, accountId));
            Assertions.fail("An exception of an existing document number should be thrown");
        } catch (BadRequestException badRequestException) {
            Assertions.assertEquals("The resource can not be created, because the document number is already registered for another user", badRequestException.getMessage());
            return;
        }

        Assertions.fail("An exception of type BadRequestException should be thrown");
    }

    @Test
    void shouldReturnGenericMessageForUserWhenAnUnexpectedExceptionOccurs() {
        Long documentNumber = 21390414L;
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountRepository.findByDocumentNumber(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(accountRepository.save(Mockito.any())).thenThrow(new RuntimeException("Generic database error"));

        try {
            accountService.persist(AccountCreationDtoMock.generateInstance(documentNumber, accountId));
        } catch (Exception exception) {
            Assertions.assertEquals("There was an error trying to persist the account " + accountId + ". Please contact the system administrator if the error continues", exception.getMessage());
        }
    }
}
