package com.esatus.ssi.bkamt.controller.verification.repository;

import com.esatus.ssi.bkamt.controller.verification.domain.PresentationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the {@link PresentationRequest} entity.
 */
@Repository
public interface PresentationRequestRepository extends MongoRepository<PresentationRequest, String> {
    Optional<PresentationRequest> findOneById(String id);
    Optional<PresentationRequest> findOneByThreadId(String threadId);
}
