package com.pismo.code.assessment.service;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDtoMock;
import com.pismo.code.assessment.domain.dto.transaction.TransactionCreationDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionCreationDtoMock;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.entity.account.AccountMock;
import com.pismo.code.assessment.domain.entity.account.OperationTypeMock;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.domain.repository.TransactionRepository;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
import com.pismo.code.assessment.exception.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private OperationTypeService operationTypeService;

    @MockitoBean
    private TransactionRepository transactionRepository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(TransactionServiceTest.class);
    }

    @Test
    void shouldReturnGenericErrorMessageForUsersWhenAnUnexpectedDatabaseExceptionOccurs() {
        BigDecimal amount = BigDecimal.valueOf(23.21);
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();

        TransactionCreationDto transactionCreationDto = TransactionCreationDtoMock.generateInstance(accountId, operationTypeId, amount);
        Mockito.when(accountService.isAccountValid(Mockito.any())).thenReturn(true);
        Mockito.when(operationTypeService.findById(Mockito.any())).thenReturn(Optional.of(OperationTypeMock.generateInstance(operationTypeId, "Payment", ChargeTypeEnum.CREDIT)));
        Mockito.when(transactionRepository.save(Mockito.any())).thenThrow(new RuntimeException("Generic Database error"));

        try {
            transactionService.persist(transactionCreationDto);
            Assertions.fail("An exception should be thrown");
        } catch (Exception exception) {
            Assertions.assertEquals("There was an error trying to persist transaction. The transaction ID is " + transactionCreationDto.getTransactionId() + ". Please contact the system administrator if the error persists", exception.getMessage());
        }
    }
}
