package com.unitrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDto {

    @Email
    private String email;

    @NotBlank
    @Length(max = 255)
    private String firstName;

    @NotBlank
    @Length(max = 255)
    private String lastName;

    @Length(min = 8, max = 255)
    private String password;

    private List<ParticipationDto> projects;

}
