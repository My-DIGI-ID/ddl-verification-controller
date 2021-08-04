package com.esatus.ssi.bkamt.controller.verification.security.agent;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AgentAuthManager implements AuthenticationManager {

    private final String apikey;

    public AgentAuthManager(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();

        if (principal.equals(this.apikey)) {
            authentication.setAuthenticated(true);
            return authentication;
        } else {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }
    }
}
