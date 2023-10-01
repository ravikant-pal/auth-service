package com.triton.auth.controller;

import com.triton.auth.dto.request.OtpAuthRequest;
import com.triton.auth.exceptions.dto.ExceptionResponse;
import com.triton.auth.service.AuthService;
import com.triton.auth.service.OtpService;
import com.triton.auth.service.TokenService;
import com.triton.auth.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(Constants.API_V1 + "/auth")
@Tag(name = "Auth Controller", description = "Info related to Auth.")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final OtpService otpService;

    @Autowired
    public AuthController(AuthService authService, TokenService tokenService, OtpService otpService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.otpService = otpService;
    }

    @Operation(summary = "Sign up and Sign in", description = "Verify OTP and generate token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful response", content = @Content(schema = @Schema(implementation = Map.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody OtpAuthRequest otpAuthRequest,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {
        return ResponseEntity.ok(otpService.verifyOtp(otpAuthRequest, request, response));
    }

    @Operation(summary = "Generate OTP", description = "Generate OTP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam @NotBlank(message = "Email must not be blank") @Email(message = "Invalid email address") String email) {
        return ResponseEntity.ok(otpService.sendOtp(email));
    }

    @PutMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@CookieValue(value = "refreshToken") String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(tokenService.getAccessTokenFromRefreshToken(refreshToken));
        }
    }

    @Operation(summary = "Log out ", description = "Clear the cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "503", description = "Service is busy or temporarily unavailable. Caller should try again.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown or unexpected error encountered", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> logout(Principal principal) {
        return ResponseEntity.ok(authService.logout(principal.getName()));
    }
}

