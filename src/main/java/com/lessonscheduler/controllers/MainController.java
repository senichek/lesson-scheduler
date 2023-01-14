package com.lessonscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.lessonscheduler.service.AuthenticationService;
import com.lessonscheduler.service.UserService;


@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;
    

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
    public ResponseEntity<AuthResponseDTO> login(@RequestBody loginDTO loginDTO) {

        AuthResponseDTO authenticated = authenticationService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        //[ROLE_USER] or [ROLE_USER] onto USER or ADMIN
        authenticated.setRole(authenticated.getRole().substring(6, authenticated.getRole().length()-1));

        return new ResponseEntity<>(authenticated, HttpStatus.OK);
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
    
        AuthResponseDTO authenticated = authenticationService.authenticate(signupDTO.getEmail(), signupDTO.getPassword());

        //[ROLE_USER] or [ROLE_USER] onto USER or ADMIN
        authenticated.setRole(authenticated.getRole().substring(6, authenticated.getRole().length()-1));

        return new ResponseEntity<>(authenticated, HttpStatus.OK);
    }
}
