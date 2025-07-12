package com.pragma.users.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record AuthenticationQuery(
        @NotNull String username,
        @NotNull String password
) {
}
