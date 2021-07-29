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

import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthManager implements AuthenticationManager {

    private final VerifierService verifierService;

    public AuthManager(VerifierService verifierService) {
        this.verifierService = verifierService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        // Check if there is a verification in the database with the given API KEY
        boolean entityExists = verifierService.verifierExists(principal);

        if (entityExists) {
            // If there is a record with the given api key, we are authenticated
            authentication.setAuthenticated(true);
            return authentication;
        } else {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }

    }

}
