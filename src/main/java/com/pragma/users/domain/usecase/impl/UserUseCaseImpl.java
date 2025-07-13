package com.pragma.users.domain.usecase.impl;


import com.pragma.users.domain.exception.RoleNotFoundException;
import com.pragma.users.domain.exception.UserAlreadyExistsException;
import com.pragma.users.domain.exception.UserNotFoundException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.EncryptPasswordPort;
import com.pragma.users.domain.spi.persistence.RoleRepositoryPort;
import com.pragma.users.domain.spi.persistence.UserRepositoryPort;
import com.pragma.users.domain.usecase.UserUseCase;
import com.pragma.users.domain.validation.Validation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUseCaseImpl implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final EncryptPasswordPort passwordEncoder;

    @Override
    public User createCustomer(User user) {
        Validation.builder(user)
                .notBlank("firstname", User::getFirstname)
                .notBlank("lastname", User::getLastname)
                .pattern("identityDocument", User::getIdentityDocument, "\\d+")
                .pattern("phoneNumber", User::getPhoneNumber, "^\\+?[0-9]{1,13}$")
                .notNull("birthdate", User::getBirthdate)
                .email("email", User::getEmail)
                .notBlank("password", User::getPassword)
                .build()
                .validate();

        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        Role role = roleRepositoryPort.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.CUSTOMER.name()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);

        return userRepositoryPort.save(user);
    }

    @Override
    public User createEmployee(User user) {
        Validation.builder(user)
                .notBlank("firstname", User::getFirstname)
                .notBlank("lastname", User::getLastname)
                .pattern("identityDocument", User::getIdentityDocument, "\\d+")
                .pattern("phoneNumber", User::getPhoneNumber, "^\\+?[0-9]{1,13}$")
                .email("email", User::getEmail)
                .notBlank("password", User::getPassword)
                .build()
                .validate();

        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        Role role = roleRepositoryPort.findByName(RoleName.EMPLOYEE)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.EMPLOYEE.name()));

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepositoryPort.save(user);
    }

    @Override
    public User createOwner(User user) {
        Validation.builder(user)
                .notBlank("firstname", User::getFirstname)
                .notBlank("lastname", User::getLastname)
                .pattern("identityDocument", User::getIdentityDocument, "\\d+")
                .pattern("phoneNumber", User::getPhoneNumber, "^\\+?[0-9]{1,13}$")
                .minYearDifference("birthdate", User::getBirthdate, 18)
                .email("email", User::getEmail)
                .notBlank("password", User::getPassword)
                .build()
                .validate();

        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        Role role = roleRepositoryPort.findByName(RoleName.OWNER)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.OWNER.name()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);

        return userRepositoryPort.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findById(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
