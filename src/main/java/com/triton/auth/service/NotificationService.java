package com.triton.auth.service;

import com.triton.auth.dto.helper.Mail;

public interface NotificationService {

    String sendEmail(Mail mail);
}
