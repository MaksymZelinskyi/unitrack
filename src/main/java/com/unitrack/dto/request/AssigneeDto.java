package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeDto {

    private Long id;
    private String role;
    private String name;
    private String avatarUrl;

}
