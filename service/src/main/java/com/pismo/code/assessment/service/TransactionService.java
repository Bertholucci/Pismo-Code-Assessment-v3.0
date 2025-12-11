package com.pismo.code.assessment.service;

import com.pismo.code.assessment.domain.dto.transaction.TransactionAmountDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionCreationDto;
import com.pismo.code.assessment.domain.dto.transaction.TransactionDto;
import com.pismo.code.assessment.domain.entity.OperationType;
import com.pismo.code.assessment.domain.entity.Transaction;
import com.pismo.code.assessment.domain.repository.TransactionRepository;
import com.pismo.code.assessment.enums.ChargeTypeEnum;
import com.pismo.code.assessment.exception.BadRequestException;
import com.pismo.code.assessment.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        calculateBalance(transaction);

        try {
            Transaction savedTransaction = this.save(mapTransactionForSaveOperation(transaction));
            return TransactionDto.mapEntity(savedTransaction);
        } catch (Exception exception) {
            log.error("There was an error trying to persist the transaction {}, for account {}, of operation type {} and amount of {} because of {}", transaction.getTransactionId(), transaction.getAccountId(), transaction.getOperationType(), transaction.getAmount(), exception.getLocalizedMessage());
            throw new RuntimeException(String.format("There was an error trying to persist transaction. The transaction ID is %s. Please contact the system administrator if the error persists", transaction.getTransactionId()));
        }
    }

    private void calculateBalance(TransactionCreationDto transaction) {
        Optional<OperationType> operationType = findOperationType(transaction.getOperationType());

        if(operationType.isPresent() && operationType.get().getChargeType().equals(ChargeTypeEnum.CREDIT)) {
            List<TransactionAmountDto> negativeOperation = transactionRepository.findOperationsByAccountIdAndNegativeBalanceOrderByEventDateAsc(transaction.getAccountId());
            BigDecimal moneyLeft = transaction.getAmount();

            for(TransactionAmountDto operation : negativeOperation) {
                boolean isTherePositiveAmountStillLeft = moneyLeft.compareTo(BigDecimal.ZERO) != 0;

                if(isTherePositiveAmountStillLeft) {
                    BigDecimal debitOperationAmount = operation.getBalance();

                    boolean isPositiveAmountLeftSmallerThanDebitOperationAmount = moneyLeft.add(debitOperationAmount).compareTo(BigDecimal.ZERO) <= 0;
                    if(isPositiveAmountLeftSmallerThanDebitOperationAmount) {
                        moneyLeft = processPositiveAmountSmallerThanDebit(operation, moneyLeft);
                        break;
                    }

                    boolean isPositiveAmountLeftBiggerThanDebitOperationAmount = moneyLeft.add(debitOperationAmount).compareTo(BigDecimal.ZERO) > 0;
                    if(isPositiveAmountLeftBiggerThanDebitOperationAmount) {
                        moneyLeft = processPositiveAmountBiggerThanDebit(operation, moneyLeft, debitOperationAmount);
                    }
                }
            }

            transaction.setBalance(moneyLeft);
            return;
        }

        transaction.setBalance(transaction.getAmount());
    }

    private BigDecimal processPositiveAmountBiggerThanDebit(TransactionAmountDto operation, BigDecimal moneyLeft, BigDecimal debitOperationAmount) {
        BigDecimal balanceToBeSavedInTheNegativeOperation;
        balanceToBeSavedInTheNegativeOperation = BigDecimal.ZERO;
        moneyLeft = moneyLeft.add(debitOperationAmount);

        transactionRepository.updateBalanceByTransactionIdAndBalance(balanceToBeSavedInTheNegativeOperation, operation.getTransactionId());
        return moneyLeft;
    }

    private BigDecimal processPositiveAmountSmallerThanDebit(TransactionAmountDto operation, BigDecimal moneyLeft) {
        BigDecimal balanceToBeSavedInTheNegativeOperation;
        BigDecimal debitOperationAmount = operation.getBalance();
        balanceToBeSavedInTheNegativeOperation = debitOperationAmount.add(moneyLeft);

        transactionRepository.updateBalanceByTransactionIdAndBalance(balanceToBeSavedInTheNegativeOperation, operation.getTransactionId());
        return BigDecimal.ZERO;
    }

    public TransactionDto searchTransaction(UUID transactionId) {
        Optional<Transaction> transaction = this.findById(transactionId);

        if(transaction.isEmpty()) {
            throw new ResourceNotFoundException(String.format("The informed transaction '%s' does not exist", transactionId));
        }

        return TransactionDto.mapEntity(transaction.get());
    }

    private Optional<Transaction> findById(UUID transactionId) {
        return transactionRepository.findById(transactionId);
    }

    private Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private void validateOperation(UUID operationTypeId, BigDecimal amount) {
        Optional<OperationType> operationType = findOperationType(operationTypeId);

        if(operationType.isEmpty()) {
            throw new BadRequestException(String.format("The specified operation type is not valid: %s", operationTypeId));
        }

        this.validateAmount(operationType.get().getChargeType(), amount);
    }

    private Optional<OperationType> findOperationType(UUID operationTypeId) {
        Optional<OperationType> operationType = operationTypeService.findById(operationTypeId);
        return operationType;
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
            .balance(transactionCreation.getBalance())
            .eventDate(LocalDateTime.now())
            .build();
    }
}
