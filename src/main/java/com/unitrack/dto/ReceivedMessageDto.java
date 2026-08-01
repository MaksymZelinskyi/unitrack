package com.unitrack.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceivedMessageDto {

    private Long id;
    //brief text for display
    private String text;
    private LocalDateTime sentAt;
    private CollaboratorInListDto sender;
}
