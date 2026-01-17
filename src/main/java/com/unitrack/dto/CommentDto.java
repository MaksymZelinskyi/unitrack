package com.unitrack.dto;

import java.time.LocalDateTime;

public record CommentDto(Long id, String text, Long replyTo, String replyToAuthor, CollaboratorInListDto author,
                         LocalDateTime createdAt) {

}
