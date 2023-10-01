package com.triton.auth.model;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document("roles")
@NoArgsConstructor
public class Role {

    @Id
    private String id;
    private String name;
    private String description;
    private boolean active;
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
    private String updaterId;
    @DBRef
    private Set<Permission> permissions = Sets.newHashSet();
}
