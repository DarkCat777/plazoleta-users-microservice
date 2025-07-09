package com.pragma.users.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerCommand {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Documento debe ser numérico")
    private String identityDocument;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,13}$", message = "Celular inválido")
    private String phoneNumber;

    @NotNull
    @Past
    private LocalDate birthdate;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
