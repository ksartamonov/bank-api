package com.netcracker.edu.kiar.bank.api.module.user.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;

    private String username;
}
