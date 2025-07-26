package com.unitrack.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CollaboratorTaskDto {

    public Long id;
    public String title;
    public String description;
    public String project;
    public LocalDateTime deadline;

}
