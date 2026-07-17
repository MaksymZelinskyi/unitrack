package com.unitrack.util.mapper;

import com.unitrack.dto.MessageDto;
import com.unitrack.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDto messageToDto(Message message);
}
