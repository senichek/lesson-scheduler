package com.lessonscheduler.models.DTO;

import lombok.Data;

@Data
public class UserDetailsUpdateDTO {
    private int id;
    private String name;
    private String email;
}
