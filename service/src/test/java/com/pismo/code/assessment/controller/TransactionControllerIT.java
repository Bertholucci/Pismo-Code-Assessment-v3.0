package com.pismo.code.assessment.controller;

import com.pismo.code.assessment.domain.dto.account.AccountCreationDto;
import com.pismo.code.assessment.domain.dto.account.AccountDto;
import com.pismo.code.assessment.domain.dto.operation.type.OperationTypeDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionCreationDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionDto;
import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.entity.OperationType;
import com.pismo.code.assessment.domain.entity.Transaction;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import com.pismo.code.assessment.domain.repository.OperationTypeRepository;
import com.pismo.code.assessment.domain.repository.TransactionRepository;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
import com.pismo.code.assessment.exception.ApiResponse;
import org.jetbrains.annotations.NotNull;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        transactionRepository.deleteAll();

        webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldGetTransactionByIdSuccessfully() {
        UUID transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(15.78);

        createAccount(accountId);
        createOperationType(operationTypeId, ChargeTypeEnum.DEBIT);

        List<Transaction> transactions = List.of(
            new Transaction(transactionId, accountId, operationTypeId, amount, LocalDateTime.now())
        );

        transactionRepository.saveAll(transactions);

        TransactionDto returnedTransaction = webClient.get()
            .uri("/v1/transaction?transactionId={id}", transactionId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TransactionDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(accountId, returnedTransaction.getAccountId());
        Assertions.assertEquals(operationTypeId, returnedTransaction.getOperationTypeId());
        Assertions.assertEquals(amount, returnedTransaction.getAmount());
        Assertions.assertEquals(transactionId, returnedTransaction.getTransactionId());
    }

    private OperationType createOperationType(UUID operationTypeId, ChargeTypeEnum chargeType) {
        String description = "Payment";

        List<OperationType> operationTypes = List.of(
            new OperationType(operationTypeId, description, chargeType)
        );

        operationTypeRepository.saveAll(operationTypes);

        return operationTypes.get(0);
    }

    private Account createAccount(UUID accountId) {
        Long documentNumber = 12338572L;

        List<Account> accounts = List.of(
            new Account(accountId, documentNumber)
        );

        accountRepository.saveAll(accounts);
        return accounts.get(0);
    }

    @Test
    void shouldReturnNotFoundStatusIfTransactionDoesNotExist() {
        webClient.get()
            .uri("/v1/transaction?transactionId={id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(-15.78);

        createAccount(accountId);
        createOperationType(operationTypeId, ChargeTypeEnum.DEBIT);

        TransactionCreationDto transaction = new TransactionCreationDto();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setOperationType(operationTypeId);

        TransactionDto returnedOperationType = webClient.post()
                .uri("/v1/transaction")
                .body(Mono.just(transaction), OperationTypeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(returnedOperationType.getTransactionId());
        Assertions.assertEquals(operationTypeId, returnedOperationType.getOperationTypeId());
        Assertions.assertEquals(accountId, returnedOperationType.getAccountId());
        Assertions.assertEquals(amount, returnedOperationType.getAmount());
    }

    @Test
    void shouldThrowErrorWhenAccountSpecifiedForTransactionDoesNotExist() {
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(-15.78);

        createOperationType(operationTypeId, ChargeTypeEnum.DEBIT);

        TransactionCreationDto transaction = new TransactionCreationDto();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setOperationType(operationTypeId);

        ApiResponse returnedOperationType = webClient.post()
                .uri("/v1/transaction")
                .body(Mono.just(transaction), OperationTypeDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals("The specified account is not valid: " + accountId, returnedOperationType.getMessage());
    }

    @Test
    void shouldThrowErrorWhenOperationTypeSpecifiedForTransactionDoesNotExist() {
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(-15.78);

        createAccount(accountId);

        TransactionCreationDto transaction = new TransactionCreationDto();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setOperationType(operationTypeId);

        ApiResponse returnedOperationType = webClient.post()
                .uri("/v1/transaction")
                .body(Mono.just(transaction), OperationTypeDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals("The specified operation type is not valid: " + operationTypeId, returnedOperationType.getMessage());
    }

    @Test
    void shouldThrowErrorWhenOperationTypeHasChargeTypeOfCreditButTheTransactionSpecifiesANegativeAmount() {
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(-15.78);

        createAccount(accountId);
        createOperationType(operationTypeId, ChargeTypeEnum.CREDIT);

        TransactionCreationDto transaction = new TransactionCreationDto();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setOperationType(operationTypeId);

        ApiResponse returnedOperationType = webClient.post()
                .uri("/v1/transaction")
                .body(Mono.just(transaction), OperationTypeDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals("The amount specified " + amount + " was registered for a CREDIT operation, therefore the amount should be positive", returnedOperationType.getMessage());
    }

    @Test
    void shouldThrowErrorWhenOperationTypeHasChargeTypeOfDeditButTheTransactionSpecifiesAPositiveAmount() {
        UUID accountId = UUID.randomUUID();
        UUID operationTypeId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(15.78);

        createAccount(accountId);
        createOperationType(operationTypeId, ChargeTypeEnum.DEBIT);

        TransactionCreationDto transaction = new TransactionCreationDto();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setOperationType(operationTypeId);

        ApiResponse returnedOperationType = webClient.post()
                .uri("/v1/transaction")
                .body(Mono.just(transaction), OperationTypeDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals("The amount specified " + amount + " was registered for a DEBIT operation, therefore the amount should be negative", returnedOperationType.getMessage());
    }
}
