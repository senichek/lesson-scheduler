package com.lessonscheduler.service;

import java.util.List;

import com.lessonscheduler.models.Lesson;

public interface LessonService {
    public Lesson create(Lesson lesson);
    public Lesson findById(int id);
    public List<Lesson> getAll();
}
