package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String title;
    private String description;
    private Long projectId;
    private LocalDateTime deadline;
    private List<Long> assignees = new ArrayList<>();

}
