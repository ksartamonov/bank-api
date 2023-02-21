package com.netcracker.edu.kiar.bank.api.module.user.service;

import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessException;
import com.netcracker.edu.kiar.bank.api.module.role.dao.Role;
import com.netcracker.edu.kiar.bank.api.module.role.dao.RoleRepository;
import com.netcracker.edu.kiar.bank.api.module.role.enums.RoleType;
import com.netcracker.edu.kiar.bank.api.module.user.UserService;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;
import com.netcracker.edu.kiar.bank.api.module.user.model.factories.UserDTOFactory;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessExceptionManager;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserCreationForm;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import static com.netcracker.edu.kiar.bank.api.module.user.model.factories.UserDTOFactory.makeUserDTO;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final BusinessExceptionManager exceptionManager;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final UserHelper userHelper;

    public UserServiceImpl(UserRepository repository, BusinessExceptionManager exceptionManager, Validator validator, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserHelper userHelper) {
        this.repository = repository;
        this.exceptionManager = exceptionManager;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userHelper = userHelper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> result = repository.findAll();
        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return result.stream().map(UserDTOFactory::makeUserDTO).toList();
    }

    @Override
    public UserDTO findByUsername(String username) {
        Optional<UserEntity> result = repository.findByUsername(username);
        if (result.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0003",
                    Map.of("username", username)
            );
            return null;
        }
        return makeUserDTO(result.get());
    }

    @Override
    public UserDTO createNewUser(UserCreationForm form) {
        validateUserCreationForm(form);

        // Logic of having more roles can be added here. Right now only one role is used.
        Role role = roleRepository.save(Role.builder().name(RoleType.user).build());
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        UserEntity entity = UserEntity.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .roles(roles)
                .build();

        validateUsernameOnConflict(entity.getUsername());
        return makeUserDTO(repository.save(entity));
    }

    @Override
    public UserDTO updateUser(UserCreationForm form) {
        Optional<UserEntity> entityOpt = repository.findByUsername(form.getUsername());
        if (entityOpt.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0003",
                    Map.of("username", form.getUsername())
                    );
        }
        UserEntity entity = entityOpt.get();
        entity.setPassword(passwordEncoder.encode(form.getPassword()));
        entity.setUsername(form.getUsername());
        entity.setFirstName(form.getFirstName());
        entity.setLastName(form.getLastName());
        return makeUserDTO(repository.save(entity));
    }

    @Override
    public UserDTO deleteUser(String username) {
        Optional<UserEntity> entityOpt = repository.findByUsername(username);

        if (entityOpt.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0003",
                    Map.of("username", username)
            );
        }

        UserEntity user = entityOpt.get();
        repository.delete(user);
        return makeUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO deleteCurrentUser() {
        UserEntity user = userHelper.getCurrentUser();
        return deleteUser(user.getUsername());
    }

    public UserDTO getCurrentUserInfo() {
        UserEntity user = userHelper.getCurrentUser();
        return makeUserDTO(user);
    }

    /**
     * Validates form of creating a new User and throws exception if form is incorrect.
     * @param form form to validate.
     * @throws BusinessException with the error code "ERR-0002" if form is incorrect.
     */
    private void validateUserCreationForm(UserCreationForm form) {
        Set<ConstraintViolation<UserCreationForm>> validationResult = validator.validate(form);

        if (!validationResult.isEmpty()) {
            exceptionManager.throwsException(
                    "ERR-0002",
                    Map.of(
                            "constraints", validationResult.stream().collect(Collectors.toMap(
                                    ConstraintViolation::getPropertyPath,
                                    ConstraintViolation::getMessage
                            ))
                    )
            );
        }
    }

    /**
     * Checks if the given username conflicts with an existing user in the database. Throws a BusinessException with the
     * error code "ERR-0001" if the username is already taken.
     * @param username the username to check
     * @throws BusinessException if the username is already taken.
     */
    private void validateUsernameOnConflict(String username) {
        if (repository.findByUsername(username).isPresent()) {
            exceptionManager.throwsException(
                    "ERR-0001",
                    Map.of("username", username)
            );
        }
    }



}
