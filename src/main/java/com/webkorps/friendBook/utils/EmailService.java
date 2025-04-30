package com.webkorps.friendBook.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public  String sendPasswordResetEmail(String to, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "utf-8");

        // Set up Thymeleaf context with dynamic content
        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String htmlContent = templateEngine.process("reset-password", context);

        helper.setTo(to);
        helper.setSubject("Reset Your Password");
        helper.setText(htmlContent, true);

        mailSender.send(message);
        return "Password reset email sent.";
    }
}