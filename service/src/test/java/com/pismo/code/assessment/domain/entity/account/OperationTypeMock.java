package com.pismo.code.assessment.domain.entity.account;

import com.pismo.code.assessment.domain.entity.OperationType;
import com.pismo.code.assessment.enums.ChargeTypeEnum;

import java.util.UUID;

public class OperationTypeMock {

    public static OperationType generateInstance(UUID operationTypeId, String description, ChargeTypeEnum chargeTypeEnum) {
        return new OperationType(operationTypeId, description, chargeTypeEnum);
    }
}
