package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorProjectDto {

    private Long projectId;
    private String title;
    private String role;
    private List<TaskInListDto> tasks = new ArrayList<>();
}
