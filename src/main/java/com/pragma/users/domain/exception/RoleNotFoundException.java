package com.pragma.users.domain.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("No existe el rol: " + roleName);
    }
}
