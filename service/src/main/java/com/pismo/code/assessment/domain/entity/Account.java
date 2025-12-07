package com.pismo.code.assessment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account implements Serializable {

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "document_number")
    private Long documentNumber;
}
