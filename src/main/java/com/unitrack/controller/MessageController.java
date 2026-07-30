package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.ReceivedMessageDto;
import com.unitrack.dto.SentMessageDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.service.MessageService;
import com.unitrack.util.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController  {

    private final MessageService messageService;
    private final AuthorizationService authorizationService;
    private final MessageMapper messageMapper;

    @GetMapping("/received")
    public String getReceivedMessages(Principal principal, Model model) {
        List<ReceivedMessageDto> messages = messageService.getMessagesByRecipient(authorizationService.getUser(principal.getName()))
                .stream()
                .map(x -> {
                    ReceivedMessageDto dto = messageMapper.messageToReceivedMessageDto(x);
                    Collaborator s = x.getSender();
                    dto.setSender(new CollaboratorInListDto(s.getId(), s.getFullName(), s.getAvatarUrl()));
                    return dto;
                }).toList();
        model.addAttribute("messages", messages);
        return "messages";
    }

    @GetMapping("/sent")
    public String getSentMessages(Principal principal, Model model) {
        List<SentMessageDto> messages = messageService.getMessagesBySender(authorizationService.getUser(principal.getName()))
                .stream()
                .map(x -> {
                    SentMessageDto dto = messageMapper.messageToSentMessageDto(x);
                    Collaborator s = x.getRecipient();
                    dto.setRecipient(new CollaboratorInListDto(s.getId(), s.getFullName(), s.getAvatarUrl()));
                    return dto;
                }).toList();
        model.addAttribute("messages", messages);
        return "messages";
    }
}
