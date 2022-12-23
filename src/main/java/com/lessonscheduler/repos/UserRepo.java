package com.lessonscheduler.repos;

import org.springframework.stereotype.Repository;

import com.lessonscheduler.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
