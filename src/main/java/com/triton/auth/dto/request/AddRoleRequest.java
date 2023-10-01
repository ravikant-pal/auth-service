package com.triton.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class AddRoleRequest {
    private String userId;
    private Set<String> roleIds;
}
