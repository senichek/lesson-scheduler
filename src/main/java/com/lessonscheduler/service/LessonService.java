package com.lessonscheduler.service;

import java.util.List;

import com.lessonscheduler.models.Lesson;

public interface LessonService {
    public Lesson create(Lesson lesson);
    public Lesson findById(int id);
    public List<Lesson> getAll();
    public Integer delete(Integer id);
    public List<Lesson> getAllUnreserved();
    public List<Lesson> getAllReservedOfLoggedIn(Integer loggedInUserId); // all reserved lessons of the logged-in user
    
}
