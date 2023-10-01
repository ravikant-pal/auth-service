package com.triton.auth.controller;

import com.triton.auth.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.API_V1 + "/resource")
@Tag(name = "Resource Controller", description = "Info related to Resource.")
public class ResourceController {


    @GetMapping("/check")
//    @PreAuthorize("hashPermission('')")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("This is Resource protected by permission");
    }
}
