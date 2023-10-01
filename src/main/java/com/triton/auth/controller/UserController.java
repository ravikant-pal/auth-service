package com.triton.auth.controller;

import com.triton.auth.dto.request.AddRoleRequest;
import com.triton.auth.exceptions.dto.ExceptionResponse;
import com.triton.auth.service.UserService;
import com.triton.auth.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Constants.API_V1 + "/user")
@Tag(name = "User Controller", description = "Info related to User.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "User Roles", description = "Add/Update User Roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping
    public ResponseEntity<Boolean> updateUserRoles(@RequestBody AddRoleRequest roleRequest) {
        return ResponseEntity.ok(userService.updateUserRoles(roleRequest));
    }

}
