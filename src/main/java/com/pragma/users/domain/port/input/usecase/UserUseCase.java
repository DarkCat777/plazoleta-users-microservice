package com.pragma.users.domain.port.input.usecase;

import com.pragma.users.domain.model.User;

public interface UserUseCase {
    User createCustomer(User user);

    User createEmployee(User user);

    User createOwner(User user);

    User findByEmail(String email);

    User findById(Long id);
}
