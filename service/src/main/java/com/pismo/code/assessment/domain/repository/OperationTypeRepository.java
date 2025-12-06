package com.pismo.code.assessment.domain.repository;

import com.pismo.code.assessment.domain.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, UUID> {

}