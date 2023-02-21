package com.netcracker.edu.kiar.bank.api.module.transaction.model.dto;

import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class TransactionDTO {
    private UUID id;
    private String accountNumber;
    private TransactionType type;
    private Integer value;
    private Instant time;
}

