package com.pragma.users.application.service;

import com.pragma.users.application.dto.request.CreateUserCommand;
import com.pragma.users.application.dto.response.UserResponse;

public interface UserService {

    UserResponse createCustomer(CreateUserCommand request);

    UserResponse createEmployee(CreateUserCommand request);

    UserResponse createOwner(CreateUserCommand request);

    UserResponse findUserById(Long id);

    UserResponse findUserByEmail(String email);

}
