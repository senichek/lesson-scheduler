package com.lessonscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lessonscheduler.models.User;
import com.lessonscheduler.models.DTO.UserDetailsUpdateDTO;
import com.lessonscheduler.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/detailsupdate")
    public ResponseEntity<User> getLessonsWithStudentNames(@RequestBody UserDetailsUpdateDTO updateDTO) throws Exception {
        User toUpdate = userService.findById(updateDTO.getId());
        if (toUpdate == null) {
            throw new Exception("Entity with id" + updateDTO.getId() + " not found");
        }

        toUpdate.setName(updateDTO.getName());
        toUpdate.setEmail(updateDTO.getEmail());

        User updated = userService.create(toUpdate);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
