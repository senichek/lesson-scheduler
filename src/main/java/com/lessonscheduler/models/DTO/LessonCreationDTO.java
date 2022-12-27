package com.lessonscheduler.models.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LessonCreationDTO {    
    private LocalDateTime from;
    private LocalDateTime to;
}
