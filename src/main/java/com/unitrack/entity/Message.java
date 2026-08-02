package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    protected Long id;

    protected String text;

    @ManyToOne
    protected Collaborator sender;

    @ManyToOne
    protected Collaborator recipient;

    protected LocalDateTime sentAt;

}
