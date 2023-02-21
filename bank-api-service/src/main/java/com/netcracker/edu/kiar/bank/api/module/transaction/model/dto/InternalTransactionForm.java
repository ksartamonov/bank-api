package com.netcracker.edu.kiar.bank.api.module.transaction.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InternalTransactionForm {

    // validated by AccountService
    private String senderNumber; // sender/receiver, current user's account number is got via security context

    // validated by AccountService
    private String receiverNumber;

    @Min(value = 0, message = "Value cannot be negative")
    private Integer value;
}
