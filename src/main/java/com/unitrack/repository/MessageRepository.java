package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipientOrderBySentAtDesc(Collaborator recipient);

    List<Message> findBySenderOrderBySentAtDesc(Collaborator sender);
}
