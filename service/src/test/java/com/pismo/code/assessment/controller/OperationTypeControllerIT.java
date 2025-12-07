package com.pismo.code.assessment.controller;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDto;
import com.pismo.code.assessment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeCreationDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeDto;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.entity.OperationType;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.domain.repository.OperationTypeRepository;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
import com.pismo.code.assessment.exception.ApiResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OperationTypeControllerIT {

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    private WebTestClient webClient;

    @LocalServerPort
    private int port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:16"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        operationTypeRepository.deleteAll();
        webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldGetOperationTypeByIdSuccessfully() {
        UUID operationTypeId = UUID.randomUUID();
        String description = "Payment";
        ChargeTypeEnum chargeType = ChargeTypeEnum.DEBIT;

        List<OperationType> operationTypes = List.of(
            new OperationType(operationTypeId, description, chargeType)
        );

        operationTypeRepository.saveAll(operationTypes);

        OperationTypeDto returnedOperationType = webClient.get()
            .uri("/v1/operation-type?operationTypeId={id}", operationTypeId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(OperationTypeDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(operationTypeId, returnedOperationType.getOperationTypeId());
        Assertions.assertEquals(description, returnedOperationType.getDescription());
        Assertions.assertEquals(chargeType, returnedOperationType.getChargeType());
    }

    @Test
    void shouldReturnNotFoundStatusIfOperationTypeDoesNotExist() {
        webClient.get()
            .uri("/v1/operation-type?operationTypeId={id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void shouldCreateOperationTypeSuccessfully() {
        String description = "Payment";
        ChargeTypeEnum chargeType = ChargeTypeEnum.DEBIT;

        OperationTypeCreationDto operationType = new OperationTypeCreationDto();
        operationType.setDescription(description);
        operationType.setChargeType(chargeType);

        OperationTypeDto returnedOperationType = webClient.post()
                .uri("/v1/operation-type")
                .body(Mono.just(operationType), OperationTypeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OperationTypeDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(returnedOperationType.getOperationTypeId());
        Assertions.assertEquals(description, returnedOperationType.getDescription());
        Assertions.assertEquals(chargeType, returnedOperationType.getChargeType());
    }
}
