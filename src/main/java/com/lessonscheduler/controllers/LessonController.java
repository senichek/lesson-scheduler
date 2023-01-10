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
import com.lessonscheduler.models.Role;
import com.lessonscheduler.models.User;
import com.lessonscheduler.models.DTO.LessonCreationDTO;
import com.lessonscheduler.security.SecurityUtils;
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

        
        // Default description
        lesson.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris laoreet sapien et mauris venenatis auctor");

        lessonService.create(lesson);

        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping(value = "/reserve/{lessonId}")
    public ResponseEntity<Lesson> reserve(@PathVariable("lessonId") Integer id) throws Exception {

        Lesson toReserve = lessonService.findById(id);

        if (toReserve == null || toReserve.isReserved() == true) {
            throw new Exception("This slot has been either taken by someone else or deleted.");
        }

        User loggedInUser = SecurityUtils.getLoggedInUser();
        // Set studentId, studentName and reserved = true and re-save the lesson
        toReserve.setStudentId(loggedInUser.getId());
        toReserve.setStudentName(loggedInUser.getName()); // this is name, not email
        toReserve.setReserved(true);
        lessonService.create(toReserve);
        
        return new ResponseEntity<>(toReserve, HttpStatus.OK);
    }

    @GetMapping(value = "/delete/{lessonId}")
    public ResponseEntity<String> delete(@PathVariable("lessonId") Integer id) {
        Integer deleted = lessonService.delete(id);
        return new ResponseEntity<>("Lesson " + deleted + " has been deleted", HttpStatus.OK);
    }

    @GetMapping(value = "/all/unreserved")
    public ResponseEntity<List<Lesson>> getAllUnreserved() {
        List<Lesson> lessons = lessonService.getAllUnreserved();
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping(value = "/all/reserved")
    public ResponseEntity<List<Lesson>> getAllReserved() throws Exception {
        // all reserved lessons of the logged-in user
        User loggedIn = SecurityUtils.getLoggedInUser();
        if (loggedIn == null) {
            throw new Exception("Authenticated user not found");
        }
        List<Lesson> lessons = lessonService.getAllReservedOfLoggedIn(loggedIn.getId());
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping(value = "/cancel/{lessonId}")
    public ResponseEntity<Lesson> cancel(@PathVariable("lessonId") Integer id) throws Exception {

        Lesson toCancel = lessonService.findById(id);

        if (toCancel == null) {
            throw new Exception("The entity not found.");
        }

        toCancel.setStudentId(null);
        toCancel.setStudentName(null); 
        toCancel.setReserved(false);

        lessonService.create(toCancel);
        
        return new ResponseEntity<>(toCancel, HttpStatus.OK);
    }

    @GetMapping(value = "/{lessonId}")
    public ResponseEntity<Lesson> getById(@PathVariable("lessonId") Integer id) throws Exception {
        // Return the lesson which is either unreserved or reserved by the logged-in user.
        // In other words we must not allow accessing a lesson reserved by a different user.

        Lesson lesson = lessonService.findById(id);

        if (lesson == null) {
            throw new Exception("The entity was not found.");
        }

        User loggedInUser = SecurityUtils.getLoggedInUser();

        if (lesson.isReserved() == true && loggedInUser.getRole().equals(Role.USER) && lesson.getStudentId() != loggedInUser.getId()) {
            throw new Exception("No permission. You cannot see other students lessons.");
        }

        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
}
