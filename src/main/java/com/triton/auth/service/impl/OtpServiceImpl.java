package com.triton.auth.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.triton.auth.dto.enums.ApplicationEnvironment;
import com.triton.auth.dto.helper.Mail;
import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.dto.request.OtpAuthRequest;
import com.triton.auth.exceptions.InvalidInputException;
import com.triton.auth.service.AuthService;
import com.triton.auth.service.CookieService;
import com.triton.auth.service.NotificationService;
import com.triton.auth.service.OtpService;
import com.triton.auth.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.triton.auth.utils.AuthUtils.*;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    private static final Integer EXPIRE_TIME = 5;
    private final Environment environment;
    private final AuthService authService;
    private final CookieService cookieService;
    private final NotificationService notificationService;
    private final LoadingCache<String, String> otpCache;

    @Autowired
    public OtpServiceImpl(AuthService authService,
                          NotificationService notificationService,
                          Environment environment, CookieService cookieService) {
        this.otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_TIME, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@Nonnull String s) {
                        return StringUtils.EMPTY;
                    }
                });
        this.authService = authService;
        this.environment = environment;
        this.cookieService = cookieService;
        this.notificationService = notificationService;
    }

    @Override
    public String sendOtp(String email) {
        String theOTP = getRandomOTP(email);
        otpCache.put(email, theOTP);
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

    private String getOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            log.error("Error getting otp from cache!!");
            return StringUtils.EMPTY;
        }
    }

    private void clearOtp(String key) {
        otpCache.invalidate(key);
    }

}
