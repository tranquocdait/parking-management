package com.huyendieu.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.huyendieu.parking.services.impl.UserDetailsServiceImpl;


@Configuration
public class UserDetailsConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
}