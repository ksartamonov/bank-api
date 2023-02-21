package com.netcracker.edu.kiar.bank.api.module.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig {

    private final String USER_MATCHER = "/api/v1/user";
    private final String ACCOUNT_MATCHER = "/api/v1/account";
    private final String TRANSACTION_MATCHER = "/api/v1/transaction";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable()
                .authorizeHttpRequests()

                .requestMatchers(USER_MATCHER + "/create_user").permitAll()
                .requestMatchers(USER_MATCHER + "/delete_current_user").hasRole("user")
                .requestMatchers(
                        USER_MATCHER + "/get_all",
                        USER_MATCHER + "/find_by_username/*",
                        USER_MATCHER + "/delete_by_username/*")
                .hasRole("admin")

                .requestMatchers(ACCOUNT_MATCHER + "/create_account",
                        ACCOUNT_MATCHER + "/delete_current_account",
                        ACCOUNT_MATCHER + "/get_current_account_info")
                .hasRole("user")
                .requestMatchers(ACCOUNT_MATCHER + "/get_all",
                        ACCOUNT_MATCHER + "/find_by_account_number/*",
                        ACCOUNT_MATCHER + "/delete_by_account_number/*")
                .hasRole("admin")

                .requestMatchers(TRANSACTION_MATCHER + "/make_external_transfer",
                        TRANSACTION_MATCHER + "/make_internal_transfer")
                .hasRole("user")
                .requestMatchers(TRANSACTION_MATCHER + "/get_transactions_by_account_number/*").hasRole("admin")

                .requestMatchers(TRANSACTION_MATCHER + "/get_current_user_transactions").hasRole("user")
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}


