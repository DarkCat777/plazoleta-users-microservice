package com.pragma.users.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pragma.users.domain.validation.errors.*;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(String field, Object rejectedValue, List<String> messages) {

    public ValidationErrorResponse(String field, Object rejectedValue, List<String> messages) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.messages = new ArrayList<>(messages);
    }

    public static ValidationErrorResponse from(ValidationError error) {
        if (error instanceof WithMessage withMessage && error instanceof WithField withField) {
            return new ValidationErrorResponse(
                    withField.getField(),
                    error.getRejectedValue(),
                    Collections.singletonList(withMessage.getMessage())
            );
        }
        return null;
    }
}
