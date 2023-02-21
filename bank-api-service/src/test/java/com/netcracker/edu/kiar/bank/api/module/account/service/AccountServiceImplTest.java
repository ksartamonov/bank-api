package com.netcracker.edu.kiar.bank.api.module.account.service;


import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountDTO;
import com.netcracker.edu.kiar.bank.api.module.account.model.dto.AccountUpdateForm;
import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessException;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
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
                .firstName("Create")
                .lastName("Correct")
                .username("+79000000001")
                .password("123456Ki")
                .build());
        AccountDTO result = service.createCurrentUserAccount();

        assertNotNull(result.getId());
        assertNotNull(result.getAccountNumber());
        assertEquals(result.getBalance(), 0);
    }

    @Test
    @WithMockUser(username = "+79000000002")
    public void createAccount_alreadyExists() {

        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Create")
                .lastName("Exists")
                .username("+79000000002")
                .password("123456Ki")
                .build());

        service.createCurrentUserAccount();

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.createCurrentUserAccount();
        });
        assertEquals("ERR-0008", ex.getCode());

    }

    @Test
    @WithMockUser(username = "+79000000003")
    public void getCurrentAccountInfo_correct() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Get")
                .lastName("Correct")
                .username("+79000000003")
                .password("123456Ki")
                .build());

        AccountDTO result = service.createCurrentUserAccount();

        assertNotNull(result.getAccountNumber());
        assertEquals(result.getBalance(), 0);
    }

    @Test
    @WithMockUser(username = "+79000000004")
    public void getCurrentAccountInfo_notExists() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Get")
                .lastName("Not")
                .username("+79000000004")
                .password("123456Ki")
                .build());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.getCurrentAccountInfo();
        });
        assertEquals("ERR-0004", ex.getCode());
    }

    @Test
    @WithMockUser(username = "+79000000005")
    public void updateAccount_correct() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Update")
                .lastName("Not")
                .username("+79000000005")
                .password("123456Ki")
                .build());

        AccountDTO currentUserAccount = service.createCurrentUserAccount();

        AccountUpdateForm form = AccountUpdateForm.builder()
                .accountNumber(currentUserAccount.getAccountNumber())
                .balance(100)
                .build();

        AccountDTO accountDTO = service.updateAccount(form);

        assertEquals(accountDTO.getAccountNumber(), form.getAccountNumber());
        assertEquals(accountDTO.getBalance(), form.getBalance());
    }

    @Test
    @WithMockUser(username = "+79000000006")
    public void updateAccount_incorrect() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .firstName("Update")
                .lastName("Not")
                .username("+79000000006")
                .password("123456Ki")
                .build());

        AccountDTO currentUserAccount = service.createCurrentUserAccount();

        AccountUpdateForm form = AccountUpdateForm.builder()
                .accountNumber(currentUserAccount.getAccountNumber())
                .balance(-100)
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.updateAccount(form);
        });
    }
}