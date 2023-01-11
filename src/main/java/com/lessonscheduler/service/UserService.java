package com.lessonscheduler.service;

import com.lessonscheduler.models.User;

public interface UserService {
    public User create(User user);
    public User findByEmail(String email);
}
