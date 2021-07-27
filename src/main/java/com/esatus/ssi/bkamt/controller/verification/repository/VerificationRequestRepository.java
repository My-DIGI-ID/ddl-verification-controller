package com.esatus.ssi.bkamt.controller.verification.repository;

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the {@link VerificationRequest} entity.
 */
@Repository
public interface VerificationRequestRepository extends MongoRepository<VerificationRequest, String> {
    Optional<VerificationRequest> findOneById(String id);
    Optional<VerificationRequest> findOneByThreadId(String threadId);
}
