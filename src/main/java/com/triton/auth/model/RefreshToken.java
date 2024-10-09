package com.triton.auth.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "refresh_token")
@Builder
public class RefreshToken {
    @Id
    private String id;
    private String email;
    private String refreshToken;
    @CreatedDate
    @Indexed(expireAfterSeconds = 360)
    private Date createdAt;
}
