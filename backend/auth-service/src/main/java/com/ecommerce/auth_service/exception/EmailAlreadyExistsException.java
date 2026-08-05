package com.ecommerce.auth_service.exception;
import com.ecommerce.auth_service.exception.EmailAlreadyExistsException;
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
