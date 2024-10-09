package com.triton.auth.service.impl;

import com.triton.auth.dto.request.AddRoleRequest;
import com.triton.auth.model.User;
import com.triton.auth.repository.UserRepository;
import com.triton.auth.service.RoleService;
import com.triton.auth.service.UserService;
import com.triton.auth.utils.Constants;
import com.triton.mscommons.exceptions.ResourceNotFoundException;
import com.triton.mscommons.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    @Override
    public User getByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            log.error("User with given email => {} not found", email);
            throw ResourceNotFoundException.build("User with email : " + email);
        }
    }

    @Override
    public boolean updateUserRoles(AddRoleRequest roleRequest) {
        User user = getById(roleRequest.getUserId());
        Set<Role> roles = roleRequest.getRoleIds().stream()
                .map(roleService::getRoleById).collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
        //todo:: logout the current user
        return Boolean.TRUE;
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            log.error("User with given email => {} not found", username);
            throw ResourceNotFoundException.build("User with username : " + username);
        }
    }

    @Override
    public User getById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            log.error("User With given Id => {} Not found", id);
            throw ResourceNotFoundException.build("User with id : " + id);
        }
    }

    @Override
    public User getOrCreateUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            log.warn("User with email id => {} does not exists, Creating new ...", email);
            user = new User();
            user.setFirstName(Constants.DEFAULT_USER_NAME);
            user.setEmail(email);
            user.setActive(Boolean.TRUE);
            user.setUsername(email.substring(0, email.indexOf("@")));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public Set<Role> getUserRolesByUserName(String username) {
        User user = getByUsername(username);
        return user.getRoles();
    }


}
