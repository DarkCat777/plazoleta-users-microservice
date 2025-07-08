package com.pragma.users.infrastructure.adapter.input.rest;

import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.domain.model.User;
import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.infrastructure.adapter.input.dto.UserResponse;
import com.pragma.users.infrastructure.adapter.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final CreateOwnerUseCase createOwnerUseCase;
    private final UserMapper userMapper;

    @Operation(summary = "Create owner user")
    @SecurityRequirement(name = "Bearer Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Owner created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Owner already exists", content = @Content)
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/owner")
    public ResponseEntity<UserResponse> createOwner(@Validated @RequestBody CreateOwnerCommand request) {
        User user = createOwnerUseCase.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }
}