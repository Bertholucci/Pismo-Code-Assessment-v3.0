package com.pismo.code.assessment.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transaction")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "operation_type")
    private OperationType operationType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "event_date")
    private LocalDateTime eventDate = LocalDateTime.now();
}
