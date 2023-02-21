package com.netcracker.edu.kiar.bank.api.module.account;

import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountUpdateForm;

import java.util.List;

public interface AccountService {
    /**
     * Returns a list of all accounts.
     *
     * @return A list of AccountDTO objects.
     */
    List<AccountDTO> getAll();

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return An AccountDTO object if found, otherwise null.
     */
    AccountDTO findByAccountNumber(String accountNumber);

    /**
     * Creates an account for the currently logged-in user.
     *
     * @return An AccountDTO object representing the created account.
     */
    AccountDTO createCurrentUserAccount();

    /**
     * Deletes an account by its account number.
     *
     * @param accountNumber The account number to delete.
     * @return An AccountDTO object representing the deleted account if found, otherwise null.
     */
    AccountDTO deleteAccount(String accountNumber);

    /**
     * Deletes the account of the currently logged-in user.
     *
     * @return An AccountDTO object representing the deleted account if found, otherwise null.
     */
    AccountDTO deleteCurrentAccount();

    /**
     * Returns information about the account of currently logged-in user.
     *
     * @return Ac AccountDTO object representing the account of currently logged-in user.
     */
    AccountDTO getCurrentAccountInfo();

    /**
     * Updates an account with the given form data.
     *
     * @param form The form data for updating the account.
     * @return An AccountDTO object representing the updated account if found, otherwise null.
     */
    AccountDTO updateAccount(AccountUpdateForm form);
}
