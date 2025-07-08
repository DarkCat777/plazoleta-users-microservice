package com.pragma.users.application.exception;

public class UnderageUserException extends RuntimeException {
    public UnderageUserException() {
        super("El usuario debe ser mayor de edad para registrarse como propietario.");
    }
}