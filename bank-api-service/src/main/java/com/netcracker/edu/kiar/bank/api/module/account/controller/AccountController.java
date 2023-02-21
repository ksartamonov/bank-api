package com.netcracker.edu.kiar.bank.api.module.account.controller;

import com.netcracker.edu.kiar.bank.api.module.account.AccountService;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AccountController class is a REST controller that handles HTTP requests related to user accounts.
 * It maps the incoming requests to the corresponding methods of the AccountService class.
 * The controller provides methods for getting all user accounts, finding an account by account number,
 * creating a new account, and deleting an account by account number or current account.
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Handles HTTP GET requests to "/api/v1/account/get_all" to retrieve all user accounts.
     * @return ResponseEntity object containing the list of AccountDTO objects and HTTP status code.
     */
    @GetMapping("/get_all")
    public ResponseEntity<List<AccountDTO>> getAll() {
        List<AccountDTO> accounts = accountService.getAll();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Handles HTTP GET requests to "/api/v1/account/find_by_account_number/{account_number}" to find an account
     * by its account number.
     * @param account_number the account number to search for.
     * @return ResponseEntity object containing the AccountDTO object and HTTP status code.
     */
    @GetMapping("/find_by_account_number/{account_number}")
    public ResponseEntity<AccountDTO> findByAccountNumber(@PathVariable String account_number) {
        AccountDTO account = accountService.findByAccountNumber(account_number);
        return ResponseEntity.ok(account);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/account/create_account" to create a new account for the current user.
     * @return ResponseEntity object containing the AccountDTO object and HTTP status code.
     */
    @PostMapping("/create_account")
    public ResponseEntity<AccountDTO> createAccount() {
        AccountDTO account =  accountService.createCurrentUserAccount();
        return ResponseEntity.ok(account);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/account/delete_by_account_number/{account_number}" to delete an account
     * by its account number.
     * @param account_number the account number of the account to delete.
     * @return ResponseEntity object containing the AccountDTO object and HTTP status code.
     */
    @DeleteMapping("/delete_by_account_number/{account_number}")
    public ResponseEntity<AccountDTO> deleteByAccountNumber(@PathVariable String account_number) {
        AccountDTO account = accountService.deleteAccount(account_number);
        return ResponseEntity.ok(account);
    }

    /**
     * Handles HTTP DELETE requests to "/api/v1/account/delete_current_account" to delete the currently logged-in
     * user's account.
     * @return ResponseEntity object containing the AccountDTO object and HTTP status code.
     */
    @DeleteMapping("/delete_current_account")
    public ResponseEntity<AccountDTO> deleteCurrentAccount() {
        AccountDTO account = accountService.deleteCurrentAccount();
        return ResponseEntity.ok(account);
    }

}