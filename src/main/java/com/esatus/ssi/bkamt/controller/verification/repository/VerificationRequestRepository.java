/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    Optional<VerificationRequest> findOneByPresentationExchangeId(String presentationExchangeId);
    Optional<VerificationRequest> findByVerificationId(String verificationId);
    Optional<VerificationRequest> deleteByVerificationId(String verificationId);
}
