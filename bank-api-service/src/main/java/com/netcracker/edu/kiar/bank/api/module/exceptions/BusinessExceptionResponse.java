package com.netcracker.edu.kiar.bank.api.module.exceptions;

import lombok.Data;

import java.util.Map;

@Data
public class BusinessExceptionResponse {

    private String              code;
    private Integer             status;
    private String              message;
    private Map<String, Object> details;

    public static BusinessExceptionResponse fromException(BusinessException exception) {
        BusinessExceptionResponse result = new BusinessExceptionResponse();

        result.code = exception.getCode();
        result.status = exception.getHttp();
        result.message = exception.getMessage();
        result.details = exception.getDetails();

        return result;
    }
}
