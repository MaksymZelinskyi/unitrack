package com.unitrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {

    @NotBlank
    @Length(max = 255)
    private String firstName;

    @NotBlank
    @Length(max = 255)
    private String lastName;

    @URL
    private String avatarUrl;

    @Email
    private String email;

    @Length(min = 8, max = 255)
    private String password;
}
