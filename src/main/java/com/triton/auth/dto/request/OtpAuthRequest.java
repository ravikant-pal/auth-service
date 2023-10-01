package com.triton.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.io.Serializable;

@Data
public class OtpAuthRequest implements Serializable {

    @NotBlank(message = "OTP must not be blank")
    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid OTP format, it must be a 6-digit number")
    private String otp;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email address")
    private String email;
}
