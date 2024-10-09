package com.triton.auth.controller;

import com.triton.auth.service.NotificationService;
import com.triton.auth.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.triton.mscommons.utils.Constants.API_V1;

@Slf4j
@Validated
@RestController
@RequestMapping(API_V1 + "/notification")
@Tag(name = "Notification Controller", description = "Info related to Notification.")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


}
