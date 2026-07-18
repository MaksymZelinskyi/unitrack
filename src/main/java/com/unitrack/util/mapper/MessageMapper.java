package com.unitrack.util.mapper;

import com.unitrack.dto.ReceivedMessageDto;
import com.unitrack.dto.SentMessageDto;
import com.unitrack.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    ReceivedMessageDto messageToReceivedMessageDto(Message message);

    SentMessageDto messageToSentMessageDto(Message message);
}
