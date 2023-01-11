package com.lessonscheduler.models.DTO;

import lombok.Data;

@Data
public class SignupDTO {
    private String name;
    private String email;
    private String password;
    private String passwordConfirm;
}
