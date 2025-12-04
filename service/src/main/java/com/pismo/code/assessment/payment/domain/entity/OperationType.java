package com.pismo.code.assessment.payment.domain.entity;

import com.pismo.code.assessment.payment.enums.ChargeTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "operation_type")
public class OperationType {

    @Id
    @Column(name = "operation_type_id")
    private UUID operationTypeId;

    @Column(name = "description")
    private String description;

    @Column(name = "charge_type")
    private ChargeTypeEnum chargeType;
}
