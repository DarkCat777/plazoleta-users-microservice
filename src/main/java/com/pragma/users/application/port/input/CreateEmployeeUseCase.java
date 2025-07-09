package com.pragma.users.application.port.input;

import com.pragma.users.application.dto.CreateEmployeeCommand;
import com.pragma.users.domain.model.User;

public interface CreateEmployeeUseCase {
    User createEmployee(CreateEmployeeCommand command);
}
