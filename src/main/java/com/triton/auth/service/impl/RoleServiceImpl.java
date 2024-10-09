package com.triton.auth.service.impl;

import com.triton.auth.repository.RoleRepository;
import com.triton.auth.service.RoleService;
import com.triton.mscommons.enums.ApplicationEnvironment;
import com.triton.mscommons.exceptions.ResourceNotFoundException;
import com.triton.mscommons.model.Role;
import com.triton.mscommons.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.triton.mscommons.utils.CommonUtils.isTargetEnvironment;


@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MongoTemplate mongoTemplate;
    private final Environment environment;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, MongoTemplate mongoTemplate, Environment environment) {
        this.roleRepository = roleRepository;
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    @Override
    public Page<Role> getAllRoles(PageRequest pageRequest, String searchTerm) {
        if (StringUtils.isNotEmpty(searchTerm)) {
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("name").regex(searchTerm, "i"),
                    Criteria.where("description").regex(searchTerm, "i")
            );
            Query query = new Query(searchCriteria);
            return new PageImpl<>(mongoTemplate.find(query, Role.class), pageRequest, mongoTemplate.count(query, Role.class));
        } else {
            return roleRepository.findAll(pageRequest);
        }
    }

    @Override
    public String addRole(Role role) {
        role.setUpdaterId(CommonUtils.getLoggedInUser().getId());
        Role savedRole = roleRepository.save(role);
        if (isTargetEnvironment(environment, ApplicationEnvironment.DEV)) {
            return savedRole.getId();
        } else {
            return "Role has been created Successfully";
        }
    }

    @Override
    public Role getRoleById(String id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if(optionalRole.isPresent()) {
            return optionalRole.get();
        } else {
            log.warn("Role with id : {} not found", id);
            throw ResourceNotFoundException.build("Role with id : " + id);
        }
    }

    @Override
    public boolean updateRole(Role role) {
        roleRepository.save(role);
        return Boolean.TRUE;
    }
}
