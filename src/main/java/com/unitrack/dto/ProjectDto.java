package com.unitrack.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ProjectDto {

    public Long id;
    public String title;
    public String description;
    public String client;
    public LocalDateTime start;
    public LocalDateTime end;

}
