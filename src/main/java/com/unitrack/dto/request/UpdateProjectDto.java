package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectDto {

    @Positive
    private Long id;

    @NotBlank
    @Length(max = 255)
    private String title;

    @Length(max = 255)
    private String description;

    @Length(max = 255)
    private String client;

    @Length(max = 255)
    private String newClient;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private List<AssigneeDto> assignees = new ArrayList<>();

    public UpdateProjectDto(String title, String description, String client, LocalDate start, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.client = client;
        this.start = start;
        this.deadline = deadline;
    }
}
