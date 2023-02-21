package com.netcracker.edu.kiar.bank.api.module.user.service;

import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * Class Helping to main UserService class. Has method of getting currently logged-in user using SecurityContextHolder.
 */
@Service
public class UserHelper {
    private final UserRepository repository;

    public UserHelper(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the current authenticated user by extracting the username from the security context holder and
     * fetching the corresponding UserEntity from the repository.
     * @return the UserEntity corresponding to the current authenticated user
     */
    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((User) principal).getUsername();
        @SuppressWarnings("all")
        UserEntity user = repository.findByUsername(username).get();
        return user;
    }

}
