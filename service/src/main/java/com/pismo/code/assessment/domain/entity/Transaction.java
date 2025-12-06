package com.pismo.code.assessment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transaction")
public class Transaction implements Serializable {

    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "operation_type_id")
    private UUID operationTypeId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "event_date")
    private LocalDateTime eventDate = LocalDateTime.now();
}
