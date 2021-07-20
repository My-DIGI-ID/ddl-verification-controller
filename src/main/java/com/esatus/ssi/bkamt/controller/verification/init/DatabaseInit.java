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

import java.time.Instant;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.esatus.ssi.bkamt.controller.verification.domain.Authority;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.domain.Verification;
import com.esatus.ssi.bkamt.controller.verification.repository.UserRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRepository;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;

@Component
public class DatabaseInit {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    @Value("${ssibk.verification.controller.admin.username}")
    private String adminUsername;

    @Value("${ssibk.verification.controller.admin.password}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        initAdminUser();

        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            initUser();
            initVerification();
        }
    }

    private void initAdminUser() {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);

        String id = "admin";
        if (this.userRepository.existsById(id) == false) {
            User adminUser = new User();
            adminUser.setId(id);
            adminUser.setLogin(adminUsername);
            adminUser.setPassword(this.passwordEncoder.encode(adminPassword));
            adminUser.setFirstName("admin");
            adminUser.setLastName("Administrator");
            adminUser.setEmail("admin@localhost");
            adminUser.setCreatedBy("system");
            adminUser.setCreatedDate(Instant.now());
            adminUser.getAuthorities().add(adminAuthority);
            userRepository.insert(adminUser);
        }
    }

    private void initUser() {
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        String id = "user-1";
        if (this.userRepository.existsById(id) == false) {
            User userUser = new User();
            userUser.setId(id);
            userUser.setLogin("user");
            userUser.setPassword(this.passwordEncoder.encode("user"));
            userUser.setFirstName("User");
            userUser.setLastName("User");
            userUser.setEmail("user@localhost");
            userUser.setCreatedBy("system");
            userUser.setCreatedDate(Instant.now());
            userUser.getAuthorities().add(userAuthority);
            userUser.setHotelId("hotel-1");
            userRepository.insert(userUser);
        }
    }

    private void initVerification() {
    	String id = "123465789";
    	if (this.verificationRepository.existsById(id) == false) {
            Verification verification = new Verification();
            verification.setId(id);
            verification.setApiKey("ABC123");
            verification.setName("esatus AG");
            verificationRepository.insert(verification);
        }
    }
}
