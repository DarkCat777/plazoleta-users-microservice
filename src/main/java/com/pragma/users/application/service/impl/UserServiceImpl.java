package com.pragma.users.application.service.impl;

import com.pragma.users.application.dto.request.CreateUserCommand;
import com.pragma.users.application.dto.response.UserResponse;
import com.pragma.users.application.mapper.UserDtoMapper;
import com.pragma.users.application.service.UserService;
import com.pragma.users.domain.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userDtoMapper;

    @Transactional
    @Override
    public UserResponse createCustomer(CreateUserCommand request) {
        return userDtoMapper.toResponse(userUseCase.createCustomer(userDtoMapper.toDomain(request)));
    }

    @Transactional
    @Override
    public UserResponse createEmployee(CreateUserCommand request) {
        return userDtoMapper.toResponse(userUseCase.createEmployee(userDtoMapper.toDomain(request)));
    }

    @Transactional
    @Override
    public UserResponse createOwner(CreateUserCommand request) {
        return userDtoMapper.toResponse(userUseCase.createOwner(userDtoMapper.toDomain(request)));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse findUserById(Long id) {
        return userDtoMapper.toResponse(userUseCase.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse findUserByEmail(String email) {
        return userDtoMapper.toResponse(userUseCase.findByEmail(email));
    }
}
