package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProjectParticipationDto {

    private Long id;
    private String title;
    private String description;
    private String role;
    private String status;
    private LocalDate deadline;
}
