package com.pismo.code.assessment.payment.service;

import com.pismo.code.assessment.payment.domain.dto.operation.type.OperationTypeCreationDto;
import com.pismo.code.assessment.payment.domain.dto.operation.type.OperationTypeDto;
import com.pismo.code.assessment.payment.domain.entity.OperationType;
import com.pismo.code.assessment.payment.domain.repository.OperationTypeRepository;
import com.pismo.code.assessment.payment.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationTypeService {

    private final OperationTypeRepository operationTypeRepository;

    public OperationTypeDto searchOperationType(UUID operationTypeId) {
        Optional<OperationType> operationType = findById(operationTypeId);

        if(operationType.isEmpty()) {
            throw new ResourceNotFoundException("There is no valid operation type active with this ID");
        }

        return OperationTypeDto.mapEntity(operationType.get());
    }

    public Optional<OperationType> findById(UUID operationTypeId) {
        return operationTypeRepository.findById(operationTypeId);
    }

    public OperationTypeDto persist(OperationTypeCreationDto operationTypeCreationRequest) {
        try {
            OperationType operationType = operationTypeRepository.save(mapOperationTypeForSaveOperation(operationTypeCreationRequest));
            return OperationTypeDto.mapEntity(operationType);
        } catch (Exception exception) {
            log.error("There was an error trying to persist the operation type {} with description {} and charge type {}, because of {}", operationTypeCreationRequest.getOperationTypeId(), operationTypeCreationRequest.getDescription(), operationTypeCreationRequest.getChargeType(), exception.getLocalizedMessage());
            throw new RuntimeException(String.format("There was an error trying to persist the operation type %s with description %s. Please contact the system administrator if the error continues", operationTypeCreationRequest.getOperationTypeId(), operationTypeCreationRequest.getDescription()));
        }
    }

    private OperationType mapOperationTypeForSaveOperation(OperationTypeCreationDto operationTypeCreationRequest) {
        return OperationType.builder()
            .operationTypeId(operationTypeCreationRequest.getOperationTypeId())
            .description(operationTypeCreationRequest.getDescription())
            .chargeType(operationTypeCreationRequest.getChargeType())
        .build();
    }

    public void deleteById(UUID operationTypeId) {
        operationTypeRepository.deleteById(operationTypeId);
    }
}
