package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeDto {

    private Long id;
    private String avatarUrl;
    private String role;
    private String name;
}
