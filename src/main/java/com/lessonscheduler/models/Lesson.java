package com.lessonscheduler.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "date_time_from", nullable = false)
    @NotNull
    private LocalDateTime dateTimeFrom; 

    @Column(name = "date_time_to", nullable = false)
    @NotNull
    private LocalDateTime dateTimeTo; 

    @Column(name = "description")
    @Size(min = 2, max = 250)
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "reserved")
    private boolean reserved;

    @Column(name = "canceled")
    private boolean canceled;
}
