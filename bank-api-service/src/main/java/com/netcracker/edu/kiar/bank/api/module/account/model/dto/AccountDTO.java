package com.netcracker.edu.kiar.bank.api.module.account.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountDTO {
    private UUID id;
    private String accountNumber;
    private Integer balance;
}
