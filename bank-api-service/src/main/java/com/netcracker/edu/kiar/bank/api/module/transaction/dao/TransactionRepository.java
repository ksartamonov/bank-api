package com.netcracker.edu.kiar.bank.api.module.transaction.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findTransactionEntitiesByAccountNumber(String accountNumber);
}
