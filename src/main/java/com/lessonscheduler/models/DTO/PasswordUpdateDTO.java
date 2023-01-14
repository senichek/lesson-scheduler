package com.lessonscheduler.models.DTO;

import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private int id;
    private String passwordOld;
    private String password;
    private String passwordConfirm;
}
