package com.netcracker.edu.kiar.bank.api.module.user.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
//@Builder
public class UserCreationForm {
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String firstName;

    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String lastName;

//    @Pattern(regexp = "^\\d{16}$") // bank card number
//    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$\n")
    @Pattern(regexp = "^\\+7\\d{10}$") // mobile phone in format +7**********
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$" ) // Min eight characters, at least one letter and one number
    private String password;

}
