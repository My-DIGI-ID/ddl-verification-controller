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


package com.esatus.ssi.bkamt.controller.verification.config;

import com.esatus.ssi.bkamt.controller.verification.security.agent.AgentAuthFilter;
import com.esatus.ssi.bkamt.controller.verification.security.agent.AgentAuthManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class AgentSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final SecurityProblemSupport problemSupport;

    public static final String API_KEY_AUTH_HEADER_NAME = "X-API-Key";

    @Value("${ssibk.verification.controller.apikey}")
    private String apikey;

    public AgentSecurityConfiguration(SecurityProblemSupport problemSupport) {
        this.problemSupport = problemSupport;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        AgentAuthFilter filter = new AgentAuthFilter(API_KEY_AUTH_HEADER_NAME);
        filter.setAuthenticationManager(new AgentAuthManager(apikey));
        // @formatter:off
        httpSecurity
            .antMatcher("/topic/present_proof")
            .csrf()
            .disable()
            .addFilter(filter)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .anyRequest().authenticated();
        // @formatter:on
    }
}
