package com.netcracker.edu.kiar.bank.api.module.role.dao;

import com.netcracker.edu.kiar.bank.api.module.role.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private RoleType name;
}
