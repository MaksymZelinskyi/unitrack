package com.unitrack.dto.request;

import com.unitrack.dto.CollaboratorInListDto;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record UpdateTaskDto(
        Long id,
        @NotBlank @Length(max = 255) String title,
        @Length(max = 255) String description,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
        List<CollaboratorInListDto> assignees) {

    public UpdateTaskDto(String title, String description, LocalDateTime deadline) {
        this(null, title, description, deadline, new ArrayList<>());
    }
}