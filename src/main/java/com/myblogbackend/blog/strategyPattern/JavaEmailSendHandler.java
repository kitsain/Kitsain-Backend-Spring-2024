package com.myblogbackend.blog.strategyPattern;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JavaEmailSendHandler {
    private final Configuration freemarkerTemplateConfig;
    private final JavaMailSender javaMailSender;

    public void send(final String templateName,
                     final SimpleMailMessage simpleMailMessage,
                     final Map<String, Object> contentBindings)
            throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String parsedHTML = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerTemplateConfig.getTemplate(templateName),
                contentBindings
        );

        mimeMessageHelper.setSubject(Optional.ofNullable(simpleMailMessage.getSubject()).orElseThrow());
        mimeMessageHelper.setFrom(Optional.ofNullable(simpleMailMessage.getFrom()).orElseThrow(), "noreply@anonymous.com");
        mimeMessageHelper.setTo(Optional.ofNullable(simpleMailMessage.getTo()).orElseThrow());
        mimeMessageHelper.setText(parsedHTML, true);

        javaMailSender.send(mimeMessage);
    }
}
