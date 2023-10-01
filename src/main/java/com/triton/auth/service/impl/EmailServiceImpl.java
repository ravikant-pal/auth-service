package com.triton.auth.service.impl;

import com.triton.auth.dto.helper.Mail;
import com.triton.auth.service.EmailService;
import com.triton.auth.service.TemplateService;
import com.triton.auth.utils.Constants;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailServiceImpl  implements EmailService {

    private final JavaMailSender emailSender;  //todo:: make notification service and use it here, Note: Notification service must have support for Email,Text,Whatsapp
    private final TemplateService templateService;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, TemplateService templateService) {
        this.emailSender = emailSender;
        this.templateService = templateService;
    }

    @Override
    public String sendEmail(Mail mail) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            if (!ArrayUtils.isEmpty(mail.getCc())) {
                helper.setCc(mail.getCc());
            }
            if (!ArrayUtils.isEmpty(mail.getBcc())) {
                helper.setBcc(mail.getBcc());
            }
            helper.setSubject(mail.getSubject());
            if (mail.isContainsTemplate()) {
                String html = templateService.parseTemplate(Constants.OTP_TEMPLATE, mail.getTemplateVariable());
                helper.setText(html, mail.isContainsTemplate());
            } else {
                helper.setText(mail.getBody());
            }
            emailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email.", e);
            throw new RuntimeException(e);
        }
        return Constants.EMAIL_SENT_SUCCESS_MSG;
    }


}
