package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Message;
import com.unitrack.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> getMessagesByRecipient(Collaborator collaborator) {
        return messageRepository.findByRecipientOrderBySentAtDesc(collaborator);
    }

    public List<Message> getMessagesBySender(Collaborator collaborator) {
        return messageRepository.findBySenderOrderBySentAtDesc(collaborator);
    }
}
