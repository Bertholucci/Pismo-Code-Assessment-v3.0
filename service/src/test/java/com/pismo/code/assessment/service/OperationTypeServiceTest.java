package com.pismo.code.assessment.service;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDtoMock;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeCreationDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeCreationDtoMock;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.entity.account.AccountMock;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.domain.repository.OperationTypeRepository;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
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

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class OperationTypeServiceTest {

    @Autowired
    @InjectMocks
    private OperationTypeService operationTypeService;

    @MockitoBean
    private OperationTypeRepository operationTypeRepository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(OperationTypeServiceTest.class);
    }

    @Test
    void shouldReturnGenericMessageForUserWhenAnUnexpectedExceptionOccursInTheDatabase() {
        String description = "Payment";
        ChargeTypeEnum chargeTypeEnum = ChargeTypeEnum.CREDIT;
        OperationTypeCreationDto operationTypeCreationDto = OperationTypeCreationDtoMock.generateInstance(description, chargeTypeEnum);

        Mockito.when(operationTypeRepository.save(Mockito.any())).thenThrow(new RuntimeException("Generic database error"));

        try {
            operationTypeService.persist(operationTypeCreationDto);
            Assertions.fail("An exception should be throw because of the exception");
        } catch (Exception exception) {
            Assertions.assertEquals("There was an error trying to persist the operation type " + operationTypeCreationDto.getOperationTypeId() + " with description Payment. Please contact the system administrator if the error continues", exception.getMessage());
        }
    }
}
