package com.lessonscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lessonscheduler.models.DTO.AuthResponseDTO;
import com.lessonscheduler.models.DTO.loginDTO;
import com.lessonscheduler.security.JwtGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MainController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;
    

    @GetMapping(value = "/")
    public String greetings() {
        return "Lesson scheduler is running...";
    }

    @GetMapping(value = "/admin")
    public String adm() {
        return "adm page";
    }

    @GetMapping(value = "/home")
    public String home() {
        return "Lesson scheduler /home page";
    }

    @GetMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody loginDTO loginDTO, HttpServletRequest req) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        
        String token = jwtGenerator.generateJWT(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }
    
}
