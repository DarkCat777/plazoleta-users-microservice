package com.pragma.users.application.port.input;

import com.pragma.users.application.dto.CreateCustomerCommand;
import com.pragma.users.domain.model.User;

public interface CreateCustomerUseCase {
    User createCustomer(CreateCustomerCommand command);
}
