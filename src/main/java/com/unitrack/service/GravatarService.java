package com.unitrack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class GravatarService {

    public String getGravatarUrl(String email, int size) {
        String normalizedEmail = email.trim().toLowerCase();
        String hash = md5(normalizedEmail);
        return "https://www.gravatar.com/avatar/" + hash +
                "?s=" + size + "&d=identicon&r=g";
    }

    public String md5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("An md5 encoding issue");
            throw new RuntimeException(e);
        }
    }
}
