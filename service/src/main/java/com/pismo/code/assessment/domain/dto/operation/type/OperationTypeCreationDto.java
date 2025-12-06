package com.pismo.code.assessment.domain.dto.operation.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OperationTypeCreationDto {

    @JsonIgnore
    private UUID operationTypeId = UUID.randomUUID();
    private String description;
    private ChargeTypeEnum chargeType;
}
