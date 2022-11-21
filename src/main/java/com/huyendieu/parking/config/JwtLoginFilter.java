package com.huyendieu.parking.config;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.model.dto.UserLoginDTO;
import com.huyendieu.parking.services.TokenAuthenticationService;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(PermissionConstant.LOGIN_URI, "POST"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        String requestBody = httpServletRequest.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper object = new ObjectMapper();
        UserLoginDTO user = object.readValue(requestBody, UserLoginDTO.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(),
                        user.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        tokenAuthenticationService.addAuthentication(response, authResult.getName());
    }

}