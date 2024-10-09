package com.triton.auth.service;

import com.triton.auth.dto.request.AddRoleRequest;
import com.triton.auth.model.User;
import com.triton.mscommons.model.Role;

import java.util.Set;

public interface UserService {
    User getByEmail(String email);
    User getByUsername(String username);
    User getOrCreateUser(String email);

    boolean updateUserRoles(AddRoleRequest roleRequest);

    User getById(String id);


    Set<Role> getUserRolesByUserName(String username);
}
