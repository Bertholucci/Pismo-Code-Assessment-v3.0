package com.pismo.code.assessment.domain.repository;

import com.pismo.code.assessment.domain.dto.transaction.TransactionAmountDto;
import com.pismo.code.assessment.domain.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE transaction SET balance = :balance WHERE transaction_id = :transactionId", nativeQuery = true)
    void updateBalanceByTransactionIdAndBalance(@Param("balance") BigDecimal balance, @Param("transactionId") UUID transactionId);

    @Query(value = "SELECT tra.transaction_id, tra.operation_type_id, tra.balance FROM transaction tra WHERE tra.account_id = :accountId AND balance < 0 ORDER BY tra.event_date ASC", nativeQuery = true)
    List<TransactionAmountDto> findOperationsByAccountIdAndNegativeBalanceOrderByEventDateAsc(UUID accountId);
}
