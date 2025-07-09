package com.pragma.users.application.exception;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con el id: " + id);
    }

    public UserNotFoundException(String email) {
        super("Usuario no encontrado con el email: " + email);
    }
}
