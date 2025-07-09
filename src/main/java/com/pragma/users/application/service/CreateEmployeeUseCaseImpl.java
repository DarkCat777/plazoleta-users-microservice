package com.pragma.users.application.service;

import com.pragma.users.application.dto.CreateEmployeeCommand;
import com.pragma.users.application.exception.RoleNotFoundException;
import com.pragma.users.application.exception.UserAlreadyExistsException;
import com.pragma.users.application.port.input.CreateEmployeeUseCase;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepositoryPort;
import com.pragma.users.domain.port.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateEmployeeUseCaseImpl implements CreateEmployeeUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final EncryptPasswordPort passwordEncoder;

    @Override
    public User createEmployee(CreateEmployeeCommand command) {
        if (userRepositoryPort.existsByEmail(command.getEmail())) {
            throw new UserAlreadyExistsException(command.getEmail());
        }
        Role role = roleRepositoryPort.findByName(RoleName.EMPLOYEE)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.EMPLOYEE.name()));
        User user = User.builder()
                .firstname(command.getFirstname())
                .lastname(command.getLastname())
                .identityDocument(command.getIdentityDocument())
                .phoneNumber(command.getPhoneNumber())
                .birthdate(command.getBirthdate())
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .role(role)
                .build();
        return userRepositoryPort.save(user);
    }
}
