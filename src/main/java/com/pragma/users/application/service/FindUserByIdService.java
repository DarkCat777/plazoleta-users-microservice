package com.pragma.users.application.service;

import com.pragma.users.application.exception.UserNotFoundException;
import com.pragma.users.application.port.input.FindUserByIdUseCase;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindUserByIdService implements FindUserByIdUseCase {

    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
