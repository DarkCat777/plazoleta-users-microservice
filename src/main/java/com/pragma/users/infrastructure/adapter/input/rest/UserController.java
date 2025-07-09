package com.pragma.users.infrastructure.adapter.input.rest;

import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.application.port.input.FindUserByIdUseCase;
import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.input.rest.response.ErrorResponse;
import com.pragma.users.infrastructure.adapter.input.rest.response.UserResponse;
import com.pragma.users.infrastructure.adapter.mapper.UserResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserByIdUseCase findUserByIdUseCase;
    private final CreateOwnerUseCase createOwnerUseCase;
    private final UserResponseMapper userMapper;

    @Operation(summary = "Find user by Id")
    @SecurityRequirement(name = "Bearer Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = findUserByIdUseCase.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toResponse(user));
    }

    @Operation(summary = "Create owner user")
    @SecurityRequirement(name = "Bearer Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Owner created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Owner already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/owner")
    public ResponseEntity<UserResponse> createOwner(@Validated @RequestBody CreateOwnerCommand request) {
        User user = createOwnerUseCase.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

}