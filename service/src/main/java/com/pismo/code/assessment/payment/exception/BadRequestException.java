package com.pismo.code.assessment.payment.exception;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
