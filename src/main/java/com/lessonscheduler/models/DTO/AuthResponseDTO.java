package com.lessonscheduler.models.DTO;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String jwtToken = "Bearer ";

    public AuthResponseDTO(String accessToken) {
        this.jwtToken = accessToken;
    }
}
