package com.netcracker.edu.kiar.bank.api.module.account.model.factories;

import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;

public class AccountDTOFactory {
    public static AccountDTO makeAccountDTO(AccountEntity account) {
        return AccountDTO.builder()
                .id(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }
}
