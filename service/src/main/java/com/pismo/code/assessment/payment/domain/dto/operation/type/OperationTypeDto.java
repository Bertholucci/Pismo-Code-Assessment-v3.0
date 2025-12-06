package com.pismo.code.assessment.payment.domain.dto.operation.type;

import com.pismo.code.assessment.payment.domain.entity.OperationType;
import com.pismo.code.assessment.payment.enums.ChargeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OperationTypeDto {

    private UUID operationTypeId;
    private String description;
    private ChargeTypeEnum chargeType;

    public static OperationTypeDto mapEntity(OperationType operationType) {
        return new OperationTypeDto(operationType.getOperationTypeId(), operationType.getDescription(), operationType.getChargeType());
    }
}
