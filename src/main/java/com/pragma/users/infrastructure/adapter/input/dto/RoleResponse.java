package com.pragma.users.infrastructure.adapter.input.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponse {

    private Long id;

    private String name;

    private String description;
}
