package com.triton.auth.model;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document("users")
public class User {

    @Id
    private String id;
    private String name;
    private String username;
    private String email;
    private boolean active;
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
    @DBRef
    private Set<Role> roles = Sets.newHashSet();

}
