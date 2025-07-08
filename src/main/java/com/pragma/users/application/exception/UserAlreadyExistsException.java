package com.pragma.users.application.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Ya existe un usuario registrado con el correo: " + email);
    }
}
