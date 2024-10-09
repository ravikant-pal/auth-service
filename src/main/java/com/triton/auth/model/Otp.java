package com.triton.auth.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document("otp")
@Builder
public class Otp {
    private String email;
    private String otp;
    @CreatedDate
    @Indexed(expireAfterSeconds = 300)
    private Date createdAt;
}
