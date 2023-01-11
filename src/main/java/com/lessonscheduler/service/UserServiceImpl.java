package com.lessonscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lessonscheduler.models.User;
import com.lessonscheduler.repos.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User create(User user) {
        User saved = userRepo.save(user);
        return saved;
    }

    @Override
    public User findByEmail(String email) {
        User found = userRepo.findByEmail(email);
        return found;
    }
}
