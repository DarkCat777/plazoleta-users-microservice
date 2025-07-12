package com.pragma.users.application.dto.request;

import java.time.LocalDate;

public record CreateUserCommand(
        String firstname,
        String lastname,
        String identityDocument,
        String phoneNumber,
        LocalDate birthdate,
        String email,
        String password
) {
}
