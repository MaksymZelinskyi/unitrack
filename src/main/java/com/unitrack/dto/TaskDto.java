package com.unitrack.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime deadline;
    private List<CollaboratorInListDto> collaborators;
}
