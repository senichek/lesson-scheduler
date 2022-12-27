package com.lessonscheduler.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lessonscheduler.models.Lesson;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Integer> {
    public Lesson findById(int id);
}
