package com.demo.jwtsecurity.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    private static ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Authentication attemptAuthentication (
            HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        AccountCredentials creds = null;

        try {
            creds = parseLoginData(req); }catch (Exception e)
        {
            throw new AuthenticationServiceException("Unable to Authenticate");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(creds.getUsername(),
                creds.getPassword());
        setDetails(req, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        tokenAuthenticationService.addAuthentication(res, auth.getName());
    }

    private AccountCredentials parseLoginData(HttpServletRequest request) throws Exception {
        StringBuffer requestBodyString = new StringBuffer();

        String line = null;

        BufferedReader reader = request.getReader();

        while ((line = reader.readLine()) != null) {
            requestBodyString.append(line);
        }

        return objectMapper.readValue(requestBodyString.toString(), new TypeReference<AccountCredentials>() {});
    }
}
