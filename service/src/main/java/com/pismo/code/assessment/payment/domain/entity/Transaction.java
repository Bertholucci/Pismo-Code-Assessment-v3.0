package com.pismo.code.assessment.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

import java.util.UUID;

@Entity(name = "transaction")
public class Transaction {

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "account_id")
    private Account account;


}
