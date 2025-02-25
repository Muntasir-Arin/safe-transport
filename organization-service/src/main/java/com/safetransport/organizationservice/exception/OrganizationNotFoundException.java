package com.safetransport.organizationservice.exception;

import lombok.EqualsAndHashCode;

public class OrganizationNotFoundException extends RuntimeException {
    private final String msg;
    public OrganizationNotFoundException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
