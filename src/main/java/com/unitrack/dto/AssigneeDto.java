package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssigneeDto {

    private Long id;
    private String avatarUrl;
    private String role;
    private String name;
}
