package com.safetransport.userservice.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserNotFoundException extends RuntimeException {

    private final String msg;

    public UserNotFoundException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}