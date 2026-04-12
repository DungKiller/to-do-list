package com.dunght.mvc.todolist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String title;
    private String status;
    private LocalDate endDate;
    private String priority;
    private String note;
    private String email;
}
