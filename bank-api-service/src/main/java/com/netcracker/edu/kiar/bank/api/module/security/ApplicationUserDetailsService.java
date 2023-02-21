package com.netcracker.edu.kiar.bank.api.module.security;

import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Value("${spring.security.user.roles}")
    private String adminRole;

    public ApplicationUserDetailsService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // checking if admin
        if(username.equals(adminUserName)) {
            return User.builder().username(adminUserName)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(adminRole).build();
        }

        Optional<UserEntity> userOpt = repository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }

        UserEntity user = userOpt.get();
        return User.builder()
                .username(user.getUsername())
                // TODO: add checking all roles
                .roles("user")
                .password(user.getPassword())
                .build();

    }

}
