package com.lessonscheduler.service;

import com.lessonscheduler.models.DTO.AuthResponseDTO;

public interface AuthenticationService {
    public AuthResponseDTO authenticate(String email, String password);
}
