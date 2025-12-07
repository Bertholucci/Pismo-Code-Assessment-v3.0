package com.pismo.code.assessment.domain.dto.operation.type;

import com.pismo.code.assessment.enums.ChargeTypeEnum;

import java.util.UUID;

public class OperationTypeCreationDtoMock {

    public static OperationTypeCreationDto generateInstance(String description, ChargeTypeEnum chargeType) {
        OperationTypeCreationDto operationTypeCreationDto = new OperationTypeCreationDto();
        operationTypeCreationDto.setChargeType(chargeType);
        operationTypeCreationDto.setOperationTypeId(UUID.randomUUID());
        operationTypeCreationDto.setDescription(description);

        return operationTypeCreationDto;
    }
}
