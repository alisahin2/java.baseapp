package com.BaseApp.baseApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String sendVerificationEmail(String toEmail) {

        String code = generateCodeWithLength(6);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baseapp.com");
        message.setTo(toEmail);
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + code);
        emailSender.send(message);
        return code;
    }

    // UUID üretir ve belirli bir uzunlukta kod döner
    public String generateCodeWithLength(int length) {
        if (length <= 0 || length > 36) { // UUID uzunluğuna göre sınır
            throw new IllegalArgumentException("Kod uzunluğu 1 ile 36 arasında olmalıdır.");
        }

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replace("-", ""); // UUID'den "-" işaretlerini kaldırır

        // Belirli bir uzunlukta kod döner
        return uuidString.substring(0, length);
    }
}
