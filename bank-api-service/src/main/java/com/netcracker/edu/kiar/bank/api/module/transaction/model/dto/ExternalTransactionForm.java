package com.netcracker.edu.kiar.bank.api.module.transaction.model.dto;

import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ExternalTransactionForm {


    private TransactionType type;
    @Pattern(regexp = "^\\d{16}$")
    private String accountNumber;
    @Min(value = 0)
    private Integer value;
}
