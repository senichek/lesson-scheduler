package com.lessonscheduler.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lessonscheduler.models.Lesson;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Integer> {
    public Lesson findById(int id);

    @Query(value = "SELECT * FROM lessons WHERE reserved = false", nativeQuery = true)
    public List<Lesson> findAllUnreserved();

    @Query(value = "SELECT * FROM lessons WHERE reserved = true AND student_id=:idOfLoggedIn", nativeQuery = true)
    public List<Lesson> findAllReservedOfLoggedIn(Integer idOfLoggedIn);
}
