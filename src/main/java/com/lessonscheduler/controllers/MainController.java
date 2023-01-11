package com.lessonscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lessonscheduler.models.Role;
import com.lessonscheduler.models.User;
import com.lessonscheduler.models.DTO.AuthResponseDTO;
import com.lessonscheduler.models.DTO.SignupDTO;
import com.lessonscheduler.models.DTO.loginDTO;
import com.lessonscheduler.security.JwtGenerator;
import com.lessonscheduler.security.UserPrincipal;
import com.lessonscheduler.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MainController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

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

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody loginDTO loginDTO, HttpServletRequest req) {

        // "authenticate" will invoke UserPrincipalDetailsService
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        Integer userId = userPrincipal.getId();
        String name = userPrincipal.getName();
        String role = userPrincipal.getAuthorities().toString();

        String token = jwtGenerator.generateJWT(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(userId, name, role, token), HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody SignupDTO signupDTO) throws Exception {

        // Check if user exists already
        User exists = userService.findByEmail(signupDTO.getEmail());
        User toCreate = null;

        if (exists != null) {
            throw new Exception("User exists. Try to log in.");
        } else {

            if (!signupDTO.getPassword().equals(signupDTO.getPasswordConfirm())) {
                throw new Exception("Both passwords must be the same.");
            }
            toCreate = new User();
            toCreate.setName(signupDTO.getName());
            toCreate.setEmail(signupDTO.getEmail());
            toCreate.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
            toCreate.setRole(Role.USER);
            toCreate = userService.create(toCreate);
        }
        
        // "authenticate" will invoke UserPrincipalDetailsService
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(toCreate.getEmail(), signupDTO.getPassword()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        Integer userId = userPrincipal.getId();
        String name = userPrincipal.getName();
        String role = userPrincipal.getAuthorities().toString();

        String token = jwtGenerator.generateJWT(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(userId, name, role, token), HttpStatus.OK);
    }
}
