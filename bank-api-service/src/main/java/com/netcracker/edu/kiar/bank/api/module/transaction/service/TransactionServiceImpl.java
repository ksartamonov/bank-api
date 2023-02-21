package com.netcracker.edu.kiar.bank.api.module.transaction.service;

import com.netcracker.edu.kiar.bank.api.module.account.AccountService;
import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountUpdateForm;
import com.netcracker.edu.kiar.bank.api.module.account.service.AccountHelper;
import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessExceptionManager;
import com.netcracker.edu.kiar.bank.api.module.transaction.TransactionService;
import com.netcracker.edu.kiar.bank.api.module.transaction.dao.TransactionEntity;
import com.netcracker.edu.kiar.bank.api.module.transaction.dao.TransactionRepository;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.InternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.TransactionDTO;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.factories.TransactionDTOFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.netcracker.edu.kiar.bank.api.module.transaction.model.factories.TransactionDTOFactory.makeTransactionDTO;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;
    private final AccountHelper accountHelper;
    private final BusinessExceptionManager exceptionManager;
    private final TransactionValidationHelper validationHelper;

    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService, AccountHelper accountHelper, BusinessExceptionManager exceptionManager, TransactionValidationHelper validationHelper) {
        this.repository = repository;
        this.accountService = accountService;
        this.accountHelper = accountHelper;
        this.exceptionManager = exceptionManager;
        this.validationHelper = validationHelper;
    }

    @Override
    public TransactionDTO makeExternalTransfer(ExternalTransactionForm form) {

        validationHelper.validateExternalTransactionForm(form);

        TransactionEntity transaction = TransactionEntity.builder()
                .type(form.getType())
                .value(form.getValue())
                .accountNumber(form.getAccountNumber())
                .build();

        AccountDTO account = accountService.findByAccountNumber(form.getAccountNumber());

        if (form.getType() == TransactionType.OUTGOING_TRANSFER) {
            if (account.getBalance() < form.getValue()) { // if account does not have enough money
                exceptionManager.throwsException(
                        "ERR-0005",
                        Map.of("required_value", form.getValue(),
                                "available_value", account.getBalance()
                        )
                );
            }
            else {
                accountService.updateAccount(
                        AccountUpdateForm.builder()
                                .accountNumber(form.getAccountNumber())
                                .balance(account.getBalance() - form.getValue())
                                .build());
            }
        }

        if (form.getType() == TransactionType.INCOMING_TRANSFER) {
            accountService.updateAccount(
                    AccountUpdateForm.builder()
                            .accountNumber(form.getAccountNumber())
                            .balance(account.getBalance() + form.getValue())
                            .build());
        }
        return makeTransactionDTO(repository.save(transaction));
    }


    @Override
    public Set<TransactionDTO> makeInternalTransfer(InternalTransactionForm form) {

        validationHelper.validateInternalTransactionForm(form);

        AccountDTO accountSender = accountService.findByAccountNumber(form.getSenderNumber());
        AccountDTO accountReceiver = accountService.findByAccountNumber(form.getReceiverNumber());

        if (accountSender.getBalance() < form.getValue()) { // if account does not have enough money
            exceptionManager.throwsException(
                    "ERR-0005",
                    Map.of("required_value", form.getValue(),
                            "available_value", accountSender.getBalance()
                    )
            );
        }

        accountService.updateAccount(
                AccountUpdateForm.builder()
                        .accountNumber(form.getSenderNumber())
                        .balance(accountSender.getBalance() - form.getValue())
                        .build());

        accountService.updateAccount(
                AccountUpdateForm.builder()
                        .accountNumber(form.getReceiverNumber())
                        .balance(accountReceiver.getBalance() + form.getValue())
                        .build());

        Set<TransactionEntity> entities = new HashSet<>();
        entities.add(TransactionEntity.builder()
                .accountNumber(form.getSenderNumber())
                .type(TransactionType.OUTGOING_TRANSFER)
                .value(form.getValue())
                .build());
        entities.add(TransactionEntity.builder()
                .accountNumber(form.getReceiverNumber())
                .type(TransactionType.INCOMING_TRANSFER)
                .value(form.getValue())
                .build());

        return repository.saveAll(entities).stream().map(
                        TransactionDTOFactory::makeTransactionDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber) {
        List<TransactionEntity> entities = repository.findTransactionEntitiesByAccountNumber(accountNumber);
        return entities.stream().map(
                        TransactionDTOFactory::makeTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getAllCurrentUserTransactions() {
        AccountEntity account = accountHelper.getCurrentUserAccount();
        return getAllTransactionsByAccountNumber(account.getAccountNumber());
    }
}
