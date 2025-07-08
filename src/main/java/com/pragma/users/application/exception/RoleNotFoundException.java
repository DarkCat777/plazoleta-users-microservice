package com.pragma.users.application.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("No existe el rol: " + roleName);
    }
}
