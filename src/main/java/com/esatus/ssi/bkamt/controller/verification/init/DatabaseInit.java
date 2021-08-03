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

package com.esatus.ssi.bkamt.controller.verification.init;

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.repository.VerifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInit {

    @Autowired
    VerifierRepository verifierRepository;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            initDevVerification();
        }
    }

    private void initDevVerification() {
        String id = "b1e17511-8b7f-45e9-9212-017773719472";
        if (!this.verifierRepository.existsById(id)) {
            Verifier verifier = new Verifier();
            verifier.setId(id);
            verifier.setName("esatus AG");
            verifier.setApiKey("$2y$10$AW0Zit2JNBcTI0UDpPmc4OM72nm86AyvoOfV7GJOP4iropj9IuyVS");
            verifierRepository.insert(verifier);
        }
    }
}
