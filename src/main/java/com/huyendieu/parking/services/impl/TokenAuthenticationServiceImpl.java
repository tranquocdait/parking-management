package com.huyendieu.parking.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.entities.UserEntity;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.repositories.UserRepository;
import com.huyendieu.parking.services.TokenAuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final String SECRET_KEY = "$2a$10$f7RMh3epXApK615P84.VpO.ElgRgkBXwba1rph974t6ur6QfAtGZG";

    @Autowired
    private UserRepository userRepository;

    public void addAuthentication(HttpServletResponse response, String username) {
        String Jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + PermissionConstant.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            response.addHeader(PermissionConstant.AUTHORIZATION_HEADER, Jwt);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new SuccessfulResponseModel(Jwt));
            writer.print(json);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(PermissionConstant.AUTHORIZATION_HEADER);
        try {
            if (token != null) {
                String username = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                if (username != null) {
                    List<GrantedAuthority> role = new ArrayList<>();
                    UserEntity userEntity = userRepository.findByUserNameAndDisableIsFalse(username).get();
                    if (userEntity == null) {
                        throw new UsernameNotFoundException("Username Not Found");
                    }
                    role.add(new SimpleGrantedAuthority(
                            userEntity.getRoleUser().getCode()
                    ));
                    return new UsernamePasswordAuthenticationToken(username, userEntity.getPassword(), role);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}