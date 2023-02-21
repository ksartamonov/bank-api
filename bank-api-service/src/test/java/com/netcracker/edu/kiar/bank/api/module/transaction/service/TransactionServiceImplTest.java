//package com.netcracker.edu.kiar.bank.api.module.transaction.service;
//
//import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
//import com.netcracker.edu.kiar.bank.api.module.account.service.AccountServiceImpl;
//import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
//import com.netcracker.edu.kiar.bank.api.module.transaction.model.dto.ExternalTransactionForm;
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(initializers = {TransactionServiceImplTest.Initializer.class})
//class TransactionServiceImplTest {
//
//    @ClassRule
//    @SuppressWarnings("all")
//    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
//            .withDatabaseName("transaction-service-test-db")
//            .withUsername("sa")
//            .withPassword("sa");
//
//    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
//                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }
//
//    @Autowired
//    private TransactionServiceImpl transactionService;
//
//    @Autowired
//    private AccountServiceImpl accountService;
//
//    @Before
//    public void setUp() {
//        AccountDTO currentUserAccount = accountService.createCurrentUserAccount();
//        System.out.println("Number:" + currentUserAccount.getAccountNumber());
//    }
//
//    @Test
//    public void makeExternalTransfer() {
//        ExternalTransactionForm form = new ExternalTransactionForm();
//        form.setType(TransactionType.OUTGOING_TRANSFER);
//    }
//
//    @Test
//    public void makeInternalTransfer() {
//    }
//
//    @Test
//    public void getAllCurrentUserTransactions() {
//    }
//}