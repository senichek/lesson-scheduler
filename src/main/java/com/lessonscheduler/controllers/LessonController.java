package com.lessonscheduler.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lessonscheduler.models.Lesson;
import com.lessonscheduler.models.DTO.LessonCreationDTO;
import com.lessonscheduler.service.LessonService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Lesson>> getLessonsWithStudentNames() {
        List<Lesson> lessons = lessonService.getAll();
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    // Create a "time slot" which can be reserved (booked) by students
    @PostMapping(value = "/create")
    public ResponseEntity<Lesson> create(@RequestBody LessonCreationDTO lessonDTO) {

        Lesson lesson = new Lesson();
        lesson.setDateTimeFrom(lessonDTO.getFrom());
        lesson.setDateTimeTo(lessonDTO.getTo());

        lessonService.create(lesson);

        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping(value = "/reserve/{lessonId}")
    public ResponseEntity<Lesson> reserve(@PathVariable("lessonId") Integer id) {

        // Set studentId, studentName and reserved = true and re-save the lesson
        Lesson toReserve = lessonService.findById(id);
        toReserve.setStudentId(2);
        toReserve.setStudentName("Student_one");
        toReserve.setReserved(true);
        lessonService.create(toReserve);
        
        return new ResponseEntity<>(toReserve, HttpStatus.OK);
    }
}
