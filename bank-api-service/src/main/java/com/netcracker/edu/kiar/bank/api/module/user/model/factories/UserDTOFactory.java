package com.netcracker.edu.kiar.bank.api.module.user.model.factories;

import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;

public class UserDTOFactory {
    public static UserDTO makeUserDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .build();
    }
}
