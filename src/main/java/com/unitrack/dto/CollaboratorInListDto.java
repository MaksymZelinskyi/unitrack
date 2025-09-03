package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollaboratorInListDto {

    private Long id;
    private String name;
    private String avatarUrl;

}
