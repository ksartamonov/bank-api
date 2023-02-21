package com.netcracker.edu.kiar.bank.api.module.account.dao;

import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID accountId;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "balance", columnDefinition = "integer default 0", nullable = false)
    private Integer balance;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id", referencedColumnName = "user_id")
//    private UserEntity user;

    @OneToOne
    private UserEntity user;

}
