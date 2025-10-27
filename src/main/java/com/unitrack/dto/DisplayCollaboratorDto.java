package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayCollaboratorDto {

    private String fullName;
    private String avatarUrl;
    private String email;
}
