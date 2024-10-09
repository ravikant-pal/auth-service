package com.triton.auth.service.impl;

import com.triton.auth.dto.helper.Mail;
import com.triton.auth.service.MessageService;
import com.triton.auth.service.NotificationService;
import com.triton.auth.service.WhatsappService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final MessageService messageService;
    private final WhatsappService whatsappService;

    @Autowired
    public NotificationServiceImpl(MessageService messageService, WhatsappService whatsappService) {
        this.messageService = messageService;
        this.whatsappService = whatsappService;
    }


    @Override
    public String sendEmail(Mail mail) {
        return "emailService.sendEmail(mail)";
    }
}
