package com.unitrack.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private Long id;
    private String text;
    private LocalDateTime sentAt;
    private CollaboratorInListDto sender;
}
