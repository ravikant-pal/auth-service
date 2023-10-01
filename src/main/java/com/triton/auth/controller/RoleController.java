package com.triton.auth.controller;

import com.triton.auth.exceptions.dto.ExceptionResponse;
import com.triton.auth.model.Role;
import com.triton.auth.service.RoleService;
import com.triton.auth.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Constants.API_V1 + "/role")
@Tag(name = "Role Controller", description = "Info related to Role.")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Add Role", description = "Create New Role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<String> addRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

    @Operation(summary = "All Roles", description = "Get All the Roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Page<Role>> getAllRoles(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false) String searchKeyword,
                                                  @RequestParam(required = false, defaultValue = "updateTime") String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(roleService.getAllRoles(pageRequest, searchKeyword));
    }

    @Operation(summary = "Role by Id", description = "Get  the Role by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getAllRoles(@PathVariable String roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @Operation(summary = "Update Role", description = "Update Existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping
    public ResponseEntity<Boolean> updateRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(role));
    }

}
