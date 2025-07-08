package com.pragma.users.domain.port.input;


import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.domain.model.User;

public interface CreateOwnerUseCase {
    User createOwner(CreateOwnerCommand command);
}
