package com.netcracker.edu.kiar.bank.api.module.user.dao;

import com.netcracker.edu.kiar.bank.api.module.account.dao.AccountEntity;
import com.netcracker.edu.kiar.bank.api.module.role.dao.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username; // Card Number

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    private AccountEntity account;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();
}
