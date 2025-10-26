package com.unitrack.dto.request;

import com.unitrack.dto.AssigneeDto;
import com.unitrack.dto.CollaboratorInListDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    @NotBlank
    @Length(max = 255)
    private String title;
    @Length(max = 255)
    private String description;
    private Long projectId;
    private String status;
    private LocalDateTime deadline;
    private List<AssigneeDto> assignees = new ArrayList<>();

}