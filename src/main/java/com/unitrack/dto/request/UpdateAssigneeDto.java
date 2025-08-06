package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAssigneeDto {

    private Long id;
    private String name;
    private String role;
}
