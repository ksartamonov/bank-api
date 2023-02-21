package com.netcracker.edu.kiar.bank.api.module.account.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateForm {
    private String accountNumber;
    private Integer balance;
}
