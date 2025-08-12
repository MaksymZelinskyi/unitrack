package com.unitrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {

    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String email;
    private String password;
}
