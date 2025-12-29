package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProjectDto {

    private Long id;
    private String title;
    private String description;
    private ProjectClientDto client;
    private LocalDate start;
    private LocalDate end;
    private String status;

}
