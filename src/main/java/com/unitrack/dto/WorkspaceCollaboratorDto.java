package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkspaceCollaboratorDto {

    private Long id;
    private String fullName;
    private String avatarUrl;
}
