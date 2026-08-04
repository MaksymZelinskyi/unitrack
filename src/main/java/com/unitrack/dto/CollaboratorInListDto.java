package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorInListDto {
    private Long id;
    private String name;
    private String avatarUrl;
    private boolean isInvited;

    public CollaboratorInListDto (Long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }
}
