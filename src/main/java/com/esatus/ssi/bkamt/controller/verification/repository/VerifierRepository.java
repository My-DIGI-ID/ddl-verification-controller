package com.esatus.ssi.bkamt.controller.verification.repository;

import java.util.Optional;

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the {@link Verifier} entity.
 */
@Repository
public interface VerifierRepository extends MongoRepository<Verifier, String> {
    Optional<Verifier> findOneByName(String name);
	Optional<Verifier> findByApiKey(String apiKey);
}
