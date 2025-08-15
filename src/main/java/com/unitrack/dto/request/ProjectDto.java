package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    @NotBlank
    @Length(max = 255)
    private String title;

    @NotBlank
    @Length(max = 255)
    private String description;

    @NotBlank
    @Length(max = 255)
    private String client;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate deadline;

    private List<AssigneeDto> assignees = new ArrayList<>();

}
