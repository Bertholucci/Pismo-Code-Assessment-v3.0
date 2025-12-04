package com.pismo.code.assessment.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "account")
public class Account {

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "document_number")
    private String documentNumber;
}
