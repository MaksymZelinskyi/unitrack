package com.unitrack.dto.request;

import com.unitrack.dto.CollaboratorInListDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {

    private Long id;
    @NotBlank
    @Length(max = 255)
    private String title;
    @Length(max = 255)
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;
    private List<CollaboratorInListDto> assignees = new ArrayList<>();

    public UpdateTaskDto(String title, String description, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}