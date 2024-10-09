package com.triton.auth.repository;

import com.triton.auth.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
}
