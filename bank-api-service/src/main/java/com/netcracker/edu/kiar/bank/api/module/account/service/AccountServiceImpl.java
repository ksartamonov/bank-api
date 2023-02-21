package com.netcracker.edu.kiar.bank.api.module.account.service;

import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountRepository;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import com.netcracker.edu.kiar.bank.api.module.account.AccountService;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountUpdateForm;
import com.netcracker.edu.kiar.bank.api.module.account.model.factories.AccountDTOFactory;
import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessExceptionManager;
import com.netcracker.edu.kiar.bank.api.module.user.UserService;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;

import com.netcracker.edu.kiar.bank.api.module.user.service.UserHelper;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.netcracker.edu.kiar.bank.api.module.account.model.factories.AccountDTOFactory.makeAccountDTO;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final UserService userService;
    private final BusinessExceptionManager exceptionManager;
    private final UserHelper userHelper;

    public AccountServiceImpl(AccountRepository repository, UserService userService, BusinessExceptionManager exceptionManager, UserHelper userHelper) {
        this.repository = repository;
        this.exceptionManager = exceptionManager;
        this.userService = userService;
        this.userHelper = userHelper;
    }

    @Override
    public List<AccountDTO> getAll() {
        List<AccountEntity> result = repository.findAll();
        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return result.stream().map(AccountDTOFactory::makeAccountDTO).toList();
    }

    @Override
    public AccountDTO findByAccountNumber(String accountNumber) {
        Optional<AccountEntity> entityOpt = repository.findByAccountNumber(accountNumber);
        checkOnEmptiness(entityOpt, accountNumber);
        return makeAccountDTO(entityOpt.get());
    }

    @Override
    public AccountDTO createCurrentUserAccount() {
        Optional<AccountEntity> entityOpt = repository.findByUser(userHelper.getCurrentUser());
        if (entityOpt.isPresent()) {
            exceptionManager.throwsException(
                    "ERR-0008",
                    Map.of("account_number", entityOpt.get().getAccountNumber())
            );
        }
        UserDTO userDTO = userService.getCurrentUserInfo();

        AccountEntity account = AccountEntity.builder()
                        .accountNumber(String.valueOf(
                                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE) // generating unique card number containing 16 digits
                                .substring(0, 16))
                                .balance(0)
                                        .user(userHelper.getCurrentUser()).build();

        return makeAccountDTO(repository.save(account));
    }

    @Override
    public AccountDTO deleteAccount(String accountNumber) {
        Optional<AccountEntity> entityOpt = repository.findByAccountNumber(accountNumber);

        if (entityOpt.isPresent()) {
            AccountEntity account = entityOpt.get();
            repository.delete(account);
            return makeAccountDTO(account);
        }

        if (entityOpt.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0004",
                    Map.of("account_number", accountNumber)
            );
        }
        return null;
    }

    @Override
    public AccountDTO deleteCurrentAccount() {
        Optional<AccountEntity> entityOpt = repository.findByUser(userHelper.getCurrentUser());
        checkOnEmptiness(entityOpt, userHelper.getCurrentUser().getUsername());

        return deleteAccount(entityOpt.get().getAccountNumber());
    }

    @Override
    public AccountDTO getCurrentAccountInfo() {
        Optional<AccountEntity> entityOpt = repository.findByUser(userHelper.getCurrentUser());

        checkOnEmptiness(entityOpt, userHelper.getCurrentUser().getUsername());
        return makeAccountDTO(entityOpt.get());
    }
    @Override
    public AccountDTO updateAccount(AccountUpdateForm form) {
        Optional<AccountEntity> entityOpt = repository.findByAccountNumber(form.getAccountNumber());
        checkOnEmptiness(entityOpt, form.getAccountNumber());

        AccountEntity entity = entityOpt.get();
        entity.setBalance(form.getBalance());
        entity.setAccountNumber(form.getAccountNumber());

        return makeAccountDTO(repository.save(entity));
    }

    /**
     * Checks "Optional AccountEntity" on emptiness, in case if it is empty throws Business exception with the identifier
     * - account_number or username
     * @param entityOpt "Optional AccountEntity" to check on emptiness
     * @param identifier account_number or username
     */
    private void checkOnEmptiness(Optional<AccountEntity> entityOpt, String identifier) {

        if (entityOpt.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0004",
                    Map.of("identifier", identifier)
            );
        }
    }
}
