package com.lessonscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lessonscheduler.models.User;
import com.lessonscheduler.models.DTO.PasswordUpdateDTO;
import com.lessonscheduler.models.DTO.UserDetailsUpdateDTO;
import com.lessonscheduler.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/detailsupdate")
    public ResponseEntity<User> detailsUpdate(@RequestBody UserDetailsUpdateDTO updateDTO) throws Exception {
        User toUpdate = userService.findById(updateDTO.getId());
        if (toUpdate == null) {
            throw new Exception("Entity with id" + updateDTO.getId() + " not found");
        }

        toUpdate.setName(updateDTO.getName());
        toUpdate.setEmail(updateDTO.getEmail());

        User updated = userService.create(toUpdate);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping(value = "/passwordupdate")
    public ResponseEntity<User> passwordUpdate(@RequestBody PasswordUpdateDTO passwordUpdateDTO) throws Exception {
        User toUpdate = userService.findById(passwordUpdateDTO.getId());
        if (toUpdate == null) {
            throw new Exception("Entity with id" + passwordUpdateDTO.getId() + " not found");
        }

        if (!passwordEncoder.matches(passwordUpdateDTO.getPasswordOld(), toUpdate.getPassword())) {
            throw new Exception("Wrong password");
        }

        if (!passwordUpdateDTO.getPassword().equals(passwordUpdateDTO.getPasswordConfirm())) {
            throw new Exception("Both passwords must be the same");
        }

        toUpdate.setPassword(passwordEncoder.encode(passwordUpdateDTO.getPassword()));

        User updated = userService.create(toUpdate);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
