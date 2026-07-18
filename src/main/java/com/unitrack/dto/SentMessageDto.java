package com.unitrack.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SentMessageDto {

    private Long id;
    private String text;
    private LocalDateTime sentAt;
    private CollaboratorInListDto recipient;

}
