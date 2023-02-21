package com.netcracker.edu.kiar.bank.api.module.transaction;

import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.InternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.TransactionDTO;

import java.util.List;
import java.util.Set;

public interface TransactionService {
    /**
     * Makes an external transfer and returns the transaction DTO. Possible external receipt of money to the account
     * and debiting money from the account.
     *
     * @param form the form for making an external transfer
     * @return the transaction DTO for the created transaction
     */
    TransactionDTO makeExternalTransfer(ExternalTransactionForm form);

    /**
     * Makes an internal transfer and returns the set of 2 transaction DTOs(sender's and receiver's).
     * Transfer between clients within the bank, debiting from the sender, replenishment - from the recipient.
     *
     * @param form the form for making an internal transfer
     * @return the set of transaction DTOs for the created transactions
     */
    Set<TransactionDTO> makeInternalTransfer(InternalTransactionForm form);

    /**
     * Retrieves a list of all transactions for a specific account number.
     *
     * @param accountNumber the account number for which to retrieve transactions.
     * @return a list of TransactionDTO objects representing the transactions for the specified account sorted by
     * transaction time (ascending).
     */
    List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);

    /**
     * Retrieves a list of all transactions for the currently logged-in user's account.
     *
     * @return a list of TransactionDTO objects representing the transactions for the current user's account sorted by
     * transaction time (ascending).
     */
    List<TransactionDTO> getAllCurrentUserTransactions();
}
