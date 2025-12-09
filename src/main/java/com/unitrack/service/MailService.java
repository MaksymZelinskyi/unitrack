package com.unitrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private final String sender;

    public MailService(JavaMailSender javaMailSender, @Value("mail.sender") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    /**
     * sends credentials to newly registered users
     * @param email user's email
     * @param password user's password
     */
    public void sendCredentials(String email, String password) {
            String subject = "UniTrack credentials";
            String text = String.format("You were given an account at UniTrack - the most complete team-management tool." +
                                    "\n Here are your credentials: \n" +
                                    "Email: %s\n" +
                                    "Password: %s",
                            email, password);
            send(email, subject, text);
    }

    public void send(String email, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            //compose the message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(sender));
            helper.setSubject(subject);
            helper.setText(text);

            helper.setTo(email); //set recipient
            log.debug("Message with recipient {} formed", email);
            javaMailSender.send(message); //send
            log.debug("Message sent to {}", email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
