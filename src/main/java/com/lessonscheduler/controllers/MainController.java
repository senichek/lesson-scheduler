package com.lessonscheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping(value = "/")
    public String greetings() {
        return "Lesson scheduler is running...";
    }
    
}
