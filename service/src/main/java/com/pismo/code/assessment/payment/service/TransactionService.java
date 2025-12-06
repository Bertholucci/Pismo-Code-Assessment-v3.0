package com.pismo.code.assessment.payment.service;

import com.pismo.code.assessment.payment.domain.dto.transaction.TransactionCreationDto;
import com.pismo.code.assessment.payment.domain.dto.transaction.TransactionDto;
import com.pismo.code.assessment.payment.domain.entity.OperationType;
import com.pismo.code.assessment.payment.domain.entity.Transaction;
import com.pismo.code.assessment.payment.domain.repository.TransactionRepository;
import com.pismo.code.assessment.payment.enums.ChargeTypeEnum;
import com.pismo.code.assessment.payment.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final OperationTypeService operationTypeService;

    public TransactionDto persist(TransactionCreationDto transaction) {
        validateAccount(transaction.getAccountId());
        validateOperation(transaction.getOperationType(), transaction.getAmount());

        try {
            Transaction savedTransaction = this.save(mapTransactionForSaveOperation(transaction));
            return TransactionDto.mapEntity(savedTransaction);
        } catch (Exception exception) {
            log.error("There was an error trying to persist the transaction {}, for account {}, of operation type {} and amount of {} because of {}", transaction.getTransactionId(), transaction.getAccountId(), transaction.getOperationType(), transaction.getAmount(), exception.getLocalizedMessage());
            throw new RuntimeException(String.format("There was an error trying to persist transaction. The transaction ID is %s. Please contact the system administrator if the error persists", transaction.getTransactionId()));
        }
    }

    private Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private void validateOperation(UUID operationTypeId, BigDecimal amount) {
        Optional<OperationType> operationType = operationTypeService.findById(operationTypeId);

        if(operationType.isEmpty()) {
            throw new BadRequestException(String.format("The specified operation type is not valid: %s", operationTypeId));
        }

        this.validateAmount(operationType.get().getChargeType(), amount);
    }

    private void validateAmount(ChargeTypeEnum chargeType, BigDecimal amount) {
        if(chargeType.equals(ChargeTypeEnum.DEBIT) && amount.compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException(String.format("The amount specified %s was registered for a DEBIT operation, therefore the amount should be negative", amount));
        }

        if(chargeType.equals(ChargeTypeEnum.CREDIT) && amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(String.format("The amount specified %s was registered for a CREDIT operation, therefore the amount should be positive", amount));
        }
    }

    private void validateAccount(UUID account) {
        if(!accountService.isAccountValid(account)) {
            throw new BadRequestException(String.format("The specified account is not valid: %s", account));
        }
    }

    private Transaction mapTransactionForSaveOperation(TransactionCreationDto transactionCreation) {
        return Transaction.builder()
            .transactionId(transactionCreation.getTransactionId())
            .accountId(transactionCreation.getAccountId())
            .operationTypeId(transactionCreation.getOperationType())
            .amount(transactionCreation.getAmount())
            .eventDate(LocalDateTime.now())
            .build();
    }
}
