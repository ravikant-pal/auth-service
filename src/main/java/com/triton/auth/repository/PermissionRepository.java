package com.triton.auth.repository;

import com.triton.auth.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    long count();
}
