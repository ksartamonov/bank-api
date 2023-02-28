package com.netcracker.edu.kiar.bank.api.module.transaction.service;

import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountRepository;
import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessException;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.InternalTransactionForm;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.TransactionDTO;
import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {TransactionServiceImplTest.Initializer.class})
public class TransactionServiceImplTest {

    @ClassRule
    @SuppressWarnings("all")
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("transaction-service-test-db")
            .withUsername("sa")
            .withPassword("sa");


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
    @Autowired
    TransactionServiceImpl service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user1 = userRepository.save(UserEntity.builder()
                .firstName("First")
                .lastName("User")
                .username("+79000000001")
                .password("123456Ki")
                .build());
        AccountEntity account1 = accountRepository.save(AccountEntity.builder()
                .accountNumber("1111222233334444")
                .balance(1000)
                .user(user1)
                .build());
        UserEntity user2 = userRepository.save(UserEntity.builder()
                .firstName("Second")
                .lastName("User")
                .username("+79000000002")
                .password("123456Ki")
                .build());
        AccountEntity account2 = accountRepository.save(AccountEntity.builder()
                .accountNumber("4444333322221111")
                .balance(1000)
                .user(user1)
                .build());
    }

    @Test
    @WithMockUser("+79000000001")
    public void makeExternalTransfer_OUTGOING() {

        ExternalTransactionForm form = new ExternalTransactionForm();
        form.setAccountNumber("1111222233334444");
        form.setType(TransactionType.OUTGOING_TRANSFER);
        form.setValue(100);

        TransactionDTO transactionDTO = service.makeExternalTransfer(form);
        assertNotNull(transactionDTO.getId());
        assertEquals(form.getAccountNumber(), transactionDTO.getAccountNumber());
        assertEquals(form.getType(), transactionDTO.getType());
        assertEquals(form.getValue(), transactionDTO.getValue());


        assertEquals(accountRepository.findByAccountNumber("1111222233334444").get().getBalance(),
                900);
    }

    @Test
    @WithMockUser("+79000000001")
    public void makeExternalTransfer_INCOMING() {

        ExternalTransactionForm form = new ExternalTransactionForm();
        form.setAccountNumber("1111222233334444");
        form.setType(TransactionType.INCOMING_TRANSFER);
        form.setValue(100);

        TransactionDTO transactionDTO = service.makeExternalTransfer(form);
        assertNotNull(transactionDTO.getId());
        assertEquals(form.getAccountNumber(), transactionDTO.getAccountNumber());
        assertEquals(form.getType(), transactionDTO.getType());
        assertEquals(form.getValue(), transactionDTO.getValue());
        assertEquals(accountRepository.findByAccountNumber("1111222233334444").get().getBalance(),
                1100);
    }

    @Test
    @WithMockUser("+79000000001")
    public void makeExternalTransfer_negativeValue() {

        ExternalTransactionForm form = new ExternalTransactionForm();
        form.setAccountNumber("1111222233334444");
        form.setType(TransactionType.INCOMING_TRANSFER);
        form.setValue(-100);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.makeExternalTransfer(form);
        });
        assertEquals("ERR-0006", ex.getCode());
    }

    @Test
    @WithMockUser("+79000000001")
    public void makeExternalTransfer_accountNotFound() {

        ExternalTransactionForm form = new ExternalTransactionForm();
        form.setAccountNumber("1111222233334445");
        form.setType(TransactionType.INCOMING_TRANSFER);
        form.setValue(100);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.makeExternalTransfer(form);
        });
        assertEquals("ERR-0004", ex.getCode());
    }
    @Test
    public void makeInternalTransfer_correct() {
        InternalTransactionForm form = new InternalTransactionForm();
        form.setSenderNumber("1111222233334444");
        form.setReceiverNumber("4444333322221111");
        form.setValue(500);

        List<TransactionDTO> transactions = service.makeInternalTransfer(form);
        TransactionDTO transaction1 = transactions.get(0); // sender
        TransactionDTO transaction2 = transactions.get(1); // receiver

        assertEquals(accountRepository.findByAccountNumber("1111222233334444").get().getBalance(),
                500);
        assertEquals(accountRepository.findByAccountNumber("4444333322221111").get().getBalance(),
                1500);

        assertEquals(transaction1.getValue(),500);
        assertEquals(transaction1.getType(), TransactionType.OUTGOING_TRANSFER);

        assertEquals(transaction2.getValue(),500);
        assertEquals(transaction2.getType(), TransactionType.INCOMING_TRANSFER);
    }

    @Test
    public void makeInternalTransfer_negativeValue() {
        InternalTransactionForm form = new InternalTransactionForm();
        form.setSenderNumber("1111222233334444");
        form.setReceiverNumber("4444333322221111");
        form.setValue(-500);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.makeInternalTransfer(form);
        });
        assertEquals("ERR-0006", ex.getCode());
    }

    @Test
    public void makeInternalTransfer_accountNotFound() {
        InternalTransactionForm form = new InternalTransactionForm();
        form.setSenderNumber("1111222233335555");
        form.setReceiverNumber("4444333322221111");
        form.setValue(500);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.makeInternalTransfer(form);
        });
        assertEquals("ERR-0004", ex.getCode());
    }
}