package com.netcracker.edu.kiar.bank.api.module.account.service;


import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {AccountServiceImplTest.Initializer.class})
public class AccountServiceImplTest {
    @ClassRule
    @SuppressWarnings("all")
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("account-service-test-db")
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
    private AccountServiceImpl service;
    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    public  void setup() {
//        UserEntity user = userRepository.save(UserEntity.builder()
//                .firstName("Ivan")
//                .lastName("Ivanov")
//                .username("+79000000001")
//                .password("123456Ki")
//                .build());
//    }


    @Test
    @WithMockUser(username = "+79000000001")
    public void createAccount_correct() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .username("+79000000001")
                .password("123456Ki")
                .build());
        AccountDTO result = service.createCurrentUserAccount();

        assertNotNull(result.getId());
        assertNotNull(result.getAccountNumber());
        assertEquals(result.getBalance(), 0);
    }
}