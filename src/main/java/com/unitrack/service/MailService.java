package com.unitrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

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
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            //compose the message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(sender));
            helper.setSubject("UniTrack credentials");
            helper.setText(
                    String.format("You were given an account at UniTrack - the most complete team-management tool." +
                                    "\n Here are your credentials: \n" +
                                    "Email: %s\n" +
                                    "Password: %s",
                            email, password)
            );

            helper.setTo(email); //set recipient
            javaMailSender.send(message); //send
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
