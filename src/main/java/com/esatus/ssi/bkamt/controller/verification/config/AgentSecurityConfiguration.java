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
            .antMatchers("/topic/present_proof").authenticated()
        .and()
            .authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }
}
