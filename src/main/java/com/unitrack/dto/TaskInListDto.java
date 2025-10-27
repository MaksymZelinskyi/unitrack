package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInListDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean completed;
}
