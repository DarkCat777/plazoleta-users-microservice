package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.request.CreateUserCommand;
import com.pragma.users.application.dto.response.ErrorResponse;
import com.pragma.users.application.dto.response.UserResponse;
import com.pragma.users.application.service.UserService;
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

    private final UserService userService;

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
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
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
    public ResponseEntity<UserResponse> createOwner(@Validated @RequestBody CreateUserCommand request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createOwner(request));
    }

    @Operation(summary = "Create employee user")
    @SecurityRequirement(name = "Bearer Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Employee already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/employee")
    public ResponseEntity<UserResponse> createEmployee(@Validated @RequestBody CreateUserCommand request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createEmployee(request));
    }

    @Operation(summary = "Create customer user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Customer already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/customer")
    public ResponseEntity<UserResponse> createCustomer(@Validated @RequestBody CreateUserCommand request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createCustomer(request));
    }
}