package com.netcracker.edu.kiar.bank.api.module.user.service;

import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessException;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserCreationForm;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {UserServiceImplTest.Initializer.class})
public class UserServiceImplTest {

    @ClassRule
    @SuppressWarnings("all")
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("user-service-test-db")
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
    private UserServiceImpl service;


    @Test
    public void createNewUser() {

        UserCreationForm form = new UserCreationForm();
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("+79991234451");
        form.setPassword("qwerty123456");

        UserDTO result = service.createNewUser(form);
        assertNotNull(result.getId());
        assertEquals(form.getFirstName(), result.getFirstName());
        assertEquals(form.getLastName(), result.getLastName());
        assertEquals(form.getUsername(), result.getUsername());
    }

    @Test
    public void createNewUser_wrongCreationForm() {

        UserCreationForm form = new UserCreationForm();
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("wrong-username");
        form.setPassword("qwerty");

        BusinessException ex = assertThrows(BusinessException.class, () -> service.createNewUser(form));

        assertEquals("ERR-0002", ex.getCode());
    }

    @Test
    public void createNewUser_usernameDuplicate() {
        UserCreationForm form = new UserCreationForm();
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("+79991234452");
        form.setPassword("qwerty123456");

        service.createNewUser(form);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.createNewUser(form));

        assertEquals("ERR-0001", ex.getCode());
    }

    @Test
    public void deleteUser() {
        UserCreationForm form = new UserCreationForm();
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("+79991234453");
        form.setPassword("qwerty123456");
        UserDTO user = service.createNewUser(form);
        String username = user.getUsername();

        UserDTO deletedUser = service.deleteUser(username);

        assertNotNull(deletedUser.getId());
        assertEquals(form.getFirstName(), deletedUser.getFirstName());
        assertEquals(form.getLastName(), deletedUser.getLastName());
        assertEquals(form.getUsername(), deletedUser.getUsername());
    }
}