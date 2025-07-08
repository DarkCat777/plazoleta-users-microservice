package com.pragma.users.application.service;

import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.application.exception.RoleNotFoundException;
import com.pragma.users.application.exception.UnderageUserException;
import com.pragma.users.application.exception.UserAlreadyExistsException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepository;
import com.pragma.users.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class CreateOwnerService implements CreateOwnerUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EncryptPasswordPort passwordEncoder;

    @Override
    public User createOwner(CreateOwnerCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new UserAlreadyExistsException(command.getEmail());
        }
        if (Period.between(command.getBirthdate(), LocalDate.now()).getYears() < 18) {
            throw new UnderageUserException();
        }
        Role role = roleRepository.findByName(RoleName.OWNER)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.OWNER.name()));
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
        return userRepository.save(user);
    }
}
