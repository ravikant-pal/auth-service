package com.triton.auth.service;

import com.triton.auth.dto.helper.Mail;

public interface EmailService {
    String sendEmail(Mail mail);
}
