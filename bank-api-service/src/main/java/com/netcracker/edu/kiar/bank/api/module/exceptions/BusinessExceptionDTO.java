package com.netcracker.edu.kiar.bank.api.module.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessExceptionDTO {
    private String code;
    private Integer http;
    private String message;
}
