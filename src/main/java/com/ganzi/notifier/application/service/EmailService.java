package com.ganzi.notifier.application.service;

import com.ganzi.notifier.application.exception.EmailSendFailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("your_email@gmail.com");

            mailSender.send(message);
            log.info("✅ Email sent completed: to={}, subject={}", to, subject);
        } catch (MailException | MessagingException e) {
            log.error("❌ Email sent failed", e);
            throw new EmailSendFailException("Email sent failed", e);
        }
    }
}