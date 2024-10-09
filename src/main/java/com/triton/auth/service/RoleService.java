package com.triton.auth.service;

import com.triton.mscommons.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RoleService {
    String addRole(Role role);
    Role getRoleById(String id);
    Page<Role> getAllRoles(PageRequest pageRequest, String searchTerm);
    boolean updateRole(Role role);

}
