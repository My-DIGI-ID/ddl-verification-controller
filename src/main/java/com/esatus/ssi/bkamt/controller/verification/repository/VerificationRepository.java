package com.esatus.ssi.bkamt.controller.verification.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.esatus.ssi.bkamt.controller.verification.domain.Verification;

/**
 * Spring Data MongoDB repository for the {@link Verification} entity.
 */
@Repository
public interface VerificationRepository extends MongoRepository<Verification, String> {

    Optional<Verification> findOneByName(String name);
}