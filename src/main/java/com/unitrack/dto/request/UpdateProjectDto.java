package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record UpdateProjectDto(
        @Positive Long id,
        @NotBlank @Length(max = 255) String title,
        @Length(max = 255) String description,
        @Length(max = 255) String client,
        @Length(max = 255) String newClient,
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deadline,
        List<AssigneeDto> assignees) {

    public UpdateProjectDto(String title, String description, String client, LocalDate start, LocalDate deadline) {
        this(null, title, description, client, null, start, deadline, new ArrayList<>());
    }
}
