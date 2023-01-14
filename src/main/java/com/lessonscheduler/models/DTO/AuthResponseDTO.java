package com.lessonscheduler.models.DTO;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String jwtToken = "Bearer ";
    private Integer id;
    private String name;
    private String email;
    private String role;

    public AuthResponseDTO(Integer id, String name, String email, String role, String accessToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.jwtToken = accessToken;
    }
}
