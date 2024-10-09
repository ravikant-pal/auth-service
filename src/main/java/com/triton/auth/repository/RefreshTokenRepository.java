package com.triton.auth.repository;

import com.triton.auth.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
}
