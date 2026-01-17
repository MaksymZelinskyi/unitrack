package com.unitrack.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CurrentUserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private String status;

}
