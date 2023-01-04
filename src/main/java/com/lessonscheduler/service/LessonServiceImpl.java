package com.lessonscheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lessonscheduler.models.Lesson;
import com.lessonscheduler.repos.LessonRepo;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepo lessonRepo;

    @Override
    public Lesson create(Lesson lesson) {
        Lesson saved = lessonRepo.save(lesson);
        return saved;
    }

    @Override
    public Lesson findById(int id) {
        Lesson lesson = lessonRepo.findById(id);
        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepo.findAll();
    }

    @Override
    public Integer delete(Integer id) {
        lessonRepo.deleteById(id);
        return id;
    }

    @Override
    public List<Lesson> getAllUnreserved() {
        return lessonRepo.findAllUnreserved();
    }
}
