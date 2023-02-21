package com.netcracker.edu.kiar.bank.api.module.transaction.service;

import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessExceptionManager;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.InternalTransactionForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionValidationHelper {

    private final BusinessExceptionManager exceptionManager;

    public TransactionValidationHelper(BusinessExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }

    public void validateExternalTransactionForm(ExternalTransactionForm form) {
        validateValueOnNegativity(form.getValue());
        validateTransactionType(form.getType().ordinal());
    }

    public void validateInternalTransactionForm(InternalTransactionForm form) {
        validateValueOnNegativity(form.getValue());
    }

    public void validateValueOnNegativity(Integer value) {
        if (value < 0) {
            exceptionManager.throwsException(
                    "ERR-0006",
                    Map.of("value", value)
            );
        }
    }

    private void validateTransactionType(Integer value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.ordinal() == value) {
                return;
            }
        }
        exceptionManager.throwsException(
                "ERR-0007",
                Map.of("value", value)
        );
    }
}
