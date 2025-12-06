package com.pismo.code.assessment.payment.domain.entity;

import com.pismo.code.assessment.payment.enums.ChargeTypeEnum;
import jakarta.persistence.*;
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
@Entity(name = "operation_type")
public class OperationType implements Serializable {

    @Id
    @Column(name = "operation_type_id")
    private UUID operationTypeId;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_type")
    private ChargeTypeEnum chargeType;
}
