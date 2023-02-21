package com.netcracker.edu.kiar.bank.api.module.transaction.controller;

import com.netcracker.edu.kiar.bank.api.module.transaction.TransactionService;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.InternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.TransactionDTO;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * The TransactionController class is a REST controller that handles HTTP requests related to transactions (money transfers).
 * It maps the incoming requests to the corresponding methods of the TransactionService class.
 * The controller provides methods for making external and internal transfers and getting all operations of selected user.
 */
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handles HTTP POST requests to "/api/v1/transaction/make_external_transfer" to make an external transfer.
     * Possible external receipt of money to the account and debiting money from the account.
     * @param form the ExternalTransactionForm containing the transfer information
     * @return a ResponseEntity containing the created TransactionDTO
     */
    @PostMapping("/make_external_transfer")
    public ResponseEntity<TransactionDTO> makeExternalTransfer(@RequestBody ExternalTransactionForm form) {
        TransactionDTO transaction = transactionService.makeExternalTransfer(form);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/transaction/make_internal_transfer" to make an internal transfer.
     * Transfer between clients within the bank, debiting from the sender, replenishment - from the recipient.
     * @param form the InternalTransactionForm containing the transfer information
     * @return a ResponseEntity containing the created TransactionDTOs
     */
    @PostMapping("/make_internal_transfer")
    public ResponseEntity<Set<TransactionDTO>> makeInternalTransfer(@RequestBody InternalTransactionForm form) {
        Set<TransactionDTO> transaction = transactionService.makeInternalTransfer(form);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/transaction/get_transactions_by_account_number/{account_number}"
     * for getting all transactions associated with an account sorted by transaction time
     * @param account_number the account number to get transactions for
     * @return a ResponseEntity containing a list of TransactionDTOs
     */
    @GetMapping("/get_transactions_by_account_number/{account_number}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumber(@PathVariable String account_number) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsByAccountNumber(account_number);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/transaction/get_current_user_transactions" for getting all transactions
     * associated with currently logged-in user's account sorted by transaction time.
     * @return a ResponseEntity containing a list of TransactionDTOs
     */
    @GetMapping("/get_current_user_transactions")
    public ResponseEntity<List<TransactionDTO>> getCurrentUserTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllCurrentUserTransactions();
        return ResponseEntity.ok(transactions);
    }

}
