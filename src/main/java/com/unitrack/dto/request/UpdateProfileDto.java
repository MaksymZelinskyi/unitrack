package com.unitrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record UpdateProfileDto(@NotBlank @Length(max = 255) String firstName,
                               @NotBlank @Length(max = 255) String lastName,
                               @URL String avatarUrl,
                               @Email String email,
                               @Length(min = 8, max = 255) String password) {

}
