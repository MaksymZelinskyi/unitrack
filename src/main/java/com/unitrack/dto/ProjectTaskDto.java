package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectTaskDto {

    private Long id;
    private String title;
    private String description;
    private List<String> assignees;
    private LocalDateTime deadline;
    private String status;

}
