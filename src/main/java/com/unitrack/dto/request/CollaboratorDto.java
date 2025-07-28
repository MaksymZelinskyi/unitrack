package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDto {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<ParticipationDto> projects;

}
