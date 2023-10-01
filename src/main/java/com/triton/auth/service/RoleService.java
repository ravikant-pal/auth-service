package com.triton.auth.service;

import com.triton.auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;

public interface RoleService {
    String addRole(Role role);
    Role getRoleById(String id);
    Page<Role> getAllRoles(PageRequest pageRequest, String searchTerm);
    boolean updateRole(Role role);

}
