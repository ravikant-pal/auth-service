package com.triton.auth.service.impl;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.triton.auth.dto.helper.Mail;
import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.dto.request.OtpAuthRequest;
import com.triton.auth.model.Otp;
import com.triton.auth.repository.OtpRepository;
import com.triton.auth.service.AuthService;
import com.triton.auth.service.CookieService;
import com.triton.auth.service.NotificationService;
import com.triton.auth.service.OtpService;
import com.triton.auth.utils.Constants;
import com.triton.mscommons.enums.ApplicationEnvironment;
import com.triton.mscommons.exceptions.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.triton.mscommons.utils.CommonUtils.generateSixDigitOTP;
import static com.triton.mscommons.utils.CommonUtils.isTargetEnvironment;


@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    private final Environment environment;
    private final OtpRepository otpRepository;
    private final AuthService authService;
    private final CookieService cookieService;
    private final NotificationService notificationService;

    @Autowired
    public OtpServiceImpl(AuthService authService,
                          NotificationService notificationService,
                          Environment environment, OtpRepository otpRepository, CookieService cookieService) {
        this.authService = authService;
        this.environment = environment;
        this.otpRepository = otpRepository;
        this.cookieService = cookieService;
        this.notificationService = notificationService;
    }

    @Override
    public String sendOtp(String email) {
        String theOTP = generateSixDigitOTP();
        otpRepository.save(Otp.builder().otp(theOTP).email(email).build());
        if (isTargetEnvironment(environment, ApplicationEnvironment.DEV)) {
            log.info("The OTP is => {}", theOTP);
            return String.format("The OTP is => %s", theOTP);
        } else {
            Map<String, Object> variable = new HashMap<>();
            variable.put("code", theOTP);
            Mail mail = Mail.getBuilder()
                    .from(Constants.EMAIL_ID)
                    .to(email)
                    .subject(Constants.MAIL_OTP_SUBJECT)
                    .containsTemplate(Boolean.TRUE)
                    .templateVariable(variable)
                    .build();
            return notificationService.sendEmail(mail);
        }
    }

/*    public String sendEmail(Mail mail) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(mail.getModel());
            String html = templateEngine.process(Constants.OTP_TEMPLATE, context);
            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());
            emailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email.", e);
            throw new RuntimeException(e);
        }
        return Constants.EMAIL_SENT_SUCCESS_MSG;
    }*/

    @Override
    public Map<String, String> verifyOtp(OtpAuthRequest otpAuthRequest, HttpServletRequest request, HttpServletResponse res) {
        Map<String, String> response = Maps.newHashMap();
        if (otpAuthRequest.getOtp().equals(getOtp(otpAuthRequest.getEmail()))) {
            Tokens tokens = authService.doLogin(otpAuthRequest.getEmail());
            response.put("message", "Otp verified successfully");
            response.put("accessToken", tokens.getAccessToken());
            res.setHeader(HttpHeaders.SET_COOKIE, cookieService.buildRefreshCookie(tokens.getRefreshToken(), request, Boolean.FALSE));
            clearOtp(otpAuthRequest.getEmail());
        } else {
            throw InvalidInputException.build("OTP : " + otpAuthRequest.getOtp());
        }
        return response;
    }

    private String getOtp(String email) {
        return otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .map(Otp::getOtp)
                .orElseGet(() -> {
                    log.warn("Otp has been expired!!");
                    return StringUtils.EMPTY;
                });
    }

    private void clearOtp(String email) {
        otpRepository.deleteByEmail(email);
    }

}
