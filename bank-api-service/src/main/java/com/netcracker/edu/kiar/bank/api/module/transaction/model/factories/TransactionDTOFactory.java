package com.netcracker.edu.kiar.bank.api.module.transaction.model.factories;

import com.netcracker.edu.kiar.bank.api.module.transaction.dao.TransactionEntity;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.TransactionDTO;

public class TransactionDTOFactory {
    public static TransactionDTO makeTransactionDTO(TransactionEntity transaction) {
        return TransactionDTO.builder()
                .id(transaction.getTransactionId())
                .accountNumber(transaction.getAccountNumber())
                .time(transaction.getCreatedAt())
                .type(transaction.getType())
                .value(transaction.getValue())
                .build();
    }
}
