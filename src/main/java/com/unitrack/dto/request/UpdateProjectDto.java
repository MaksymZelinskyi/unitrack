package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectDto {

    private Long id;
    private String title;
    private String description;
    private String client;
    private LocalDate start;
    private LocalDate deadline;
    private List<UpdateAssigneeDto> assignees = new ArrayList<>();
}
