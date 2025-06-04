package com.task_administrator_back.auth.exception;

public class JWTTokenNotProvidedException extends RuntimeException{

    public JWTTokenNotProvidedException(String message) {
        super(message);
    }

}
