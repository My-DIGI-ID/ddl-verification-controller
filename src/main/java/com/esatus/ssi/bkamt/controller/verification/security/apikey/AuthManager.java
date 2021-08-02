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

package com.esatus.ssi.bkamt.controller.verification.security.apikey;

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

public class AuthManager implements AuthenticationManager {

    private final VerifierService verifierService;

    public AuthManager(VerifierService verifierService) {
        this.verifierService = verifierService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
//        String apiKey = (String) authentication.getPrincipal();
//
//        List<Verifier> allVerifiers = verifierService.getAll();
//        Optional<Verifier> optionalVerifier = CheckVerifierPresence(apiKey, allVerifiers);
//
//        boolean entityExists = optionalVerifier.isPresent();
//
//        if (entityExists) {
//            authentication.setAuthenticated(true);
//            return authentication;
//        } else {
//            throw new BadCredentialsException("The API key was not found or not the expected value.");
//        }
        return authentication;
    }

    private Optional<Verifier> CheckVerifierPresence(String apiKey, List<Verifier> allVerifiers) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return allVerifiers.stream().filter((Verifier verifier) -> bCryptPasswordEncoder.matches(apiKey, verifier.getApiKey())).findFirst();
    }

}
