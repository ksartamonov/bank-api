package com.netcracker.edu.kiar.bank.api.module.transaction.dao;

import com.netcracker.edu.kiar.bank.api.module.transaction.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID transactionId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "time")
    @Builder.Default
    Instant createdAt = Instant.now();

    @Column(name = "value")
    private Integer value;

}
