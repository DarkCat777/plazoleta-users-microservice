package com.pragma.users.application.port.input;

import com.pragma.users.domain.model.User;

public interface FindUserByIdUseCase {
    User getById(Long id);
}