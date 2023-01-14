package com.lessonscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lessonscheduler.models.DTO.AuthResponseDTO;
import com.lessonscheduler.security.JwtGenerator;
import com.lessonscheduler.security.UserPrincipal;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Override
    public AuthResponseDTO authenticate(String email, String password) {
        // "authenticate" will invoke UserPrincipalDetailsService
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        Integer userId = userPrincipal.getId();
        String name = userPrincipal.getName();
        String eml = userPrincipal.getUsername(); // email
        String role = userPrincipal.getAuthorities().toString();

        String token = jwtGenerator.generateJWT(authentication);

        return new AuthResponseDTO(userId, name, eml, role, token);
    }
}
