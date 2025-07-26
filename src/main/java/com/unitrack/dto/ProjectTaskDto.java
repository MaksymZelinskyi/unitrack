package com.unitrack.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class ProjectTaskDto {

    public Long id;
    public String title;
    public String description;
    public List<String> assignees;
    public LocalDateTime deadline;
    public String status;

}
