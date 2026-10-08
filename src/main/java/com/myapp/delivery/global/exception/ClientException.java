package com.myapp.delivery.global.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private ErrorCode errorCode;

    public ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}