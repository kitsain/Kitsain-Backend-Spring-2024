package com.myblogbackend.blog.strategyPatternV2;

import com.myblogbackend.blog.models.UserEntity;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class GmailStrategy implements MailStrategy {
    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(GmailStrategy.class);
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    @Override
    public void sendActivationEmail(UserEntity user) {
        log.info("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, emailProperties.getRegistrationConfirmation());
    }

    private void sendEmail(String to, String subject, String content, boolean isHtml) {
        log.info("Send email[html '{}'] to '{}' with subject '{}'", isHtml, to, subject);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(new InternetAddress(emailProperties.getRegistrationConfirmation().getFrom(), "JACK SPARROW"));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Sent email to '{}'", to);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    private void sendEmailFromTemplate(UserEntity user, EmailProperties.Email email) {
        Context context = new Context();
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, email.getBaseUrl());
        String content = templateEngine.process(email.getTemplate(), context);
        String subject = email.getSubject();
        sendEmail(user.getEmail(), subject, content, true);
    }

}
