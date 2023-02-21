package com.netcracker.edu.kiar.bank.api.module.account.service;

import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountRepository;
import com.netcracker.edu.kiar.bank.api.module.user.service.UserHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class helping to work with currently logged-in user account(SecurityContext).
 */
@Component
public class AccountHelper {
    private final UserHelper userHelper;
    private final AccountRepository accountRepository;

    public AccountHelper(UserHelper userHelper, AccountRepository accountRepository) {
        this.userHelper = userHelper;
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves the current authenticated user's account by using UserHelper class which works with SecurityContext.
     * @return the AccountEntity corresponding to the current authenticated user;
     */
    public AccountEntity getCurrentUserAccount() {
        Optional<AccountEntity> currentUserAccount = accountRepository.findByUser(userHelper.getCurrentUser());
        return currentUserAccount.get();
    }
}
