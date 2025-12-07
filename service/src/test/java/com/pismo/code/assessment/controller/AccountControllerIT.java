package com.pismo.code.assessment.controller;

import com.pismo.code.assessment.domain.entity.Account;
import com.pismo.code.assessment.domain.repository.AccountRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {

    @Autowired
    private AccountRepository accountRepository;

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
        accountRepository.deleteAll();
        webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldGetAccountByIdSuccessfully() {
        UUID accountId = UUID.randomUUID();
        Long documentNumber = 12338572L;

        List<Account> accounts = List.of(
            new Account(accountId, documentNumber)
        );

        accountRepository.saveAll(accounts);

        Account returnedAccount = webClient.get()
            .uri("/v1/account?accountId={id}", accountId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Account.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(accountId, returnedAccount.getAccountId());
        Assertions.assertEquals(documentNumber, returnedAccount.getDocumentNumber());
    }

    @Test
    void shouldReturnNotFoundStatusIfAccountDoesNotExist() {
        webClient.get()
            .uri("/v1/account?accountId={id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound();
    }
}
