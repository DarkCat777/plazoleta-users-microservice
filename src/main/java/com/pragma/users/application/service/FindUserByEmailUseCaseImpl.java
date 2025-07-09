package com.pragma.users.application.service;

import com.pragma.users.application.exception.UserNotFoundException;
import com.pragma.users.application.port.input.FindUserByEmailUseCase;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindUserByEmailUseCaseImpl implements FindUserByEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User getByEmail(String email) {
        return userRepositoryPort.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }
}
