package com.unitrack.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ProjectDto {

    private String title;
    private String description;
    private String client;
    private LocalDate start;
    private LocalDate deadline;
    private List<AssigneeDto> assignees = new ArrayList<>();

}
