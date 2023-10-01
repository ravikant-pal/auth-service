package com.triton.auth.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("permissions")
public class Permission {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean active;
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
    private String updater;

}
