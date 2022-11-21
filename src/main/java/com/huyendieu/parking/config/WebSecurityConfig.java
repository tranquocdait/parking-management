package com.huyendieu.parking.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.huyendieu.parking.constants.PermissionConstant;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() {
        try {
            return new JwtLoginFilter(authenticationManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.addAllowedOriginPattern("*");
            config.setExposedHeaders(Arrays.asList("Authorization", "totalPage", "pageCurrent"));
            config.setAllowCredentials(true);
            return config;
        });

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PermissionConstant.LOGIN_URI).permitAll()
                .antMatchers(HttpMethod.GET, "/getAll").hasRole(PermissionConstant.RoleCode.ADMIN.getCode())
//                .antMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/categories/{id}").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
