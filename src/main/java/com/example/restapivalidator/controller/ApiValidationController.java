package com.example.restapivalidator.controller;

import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.service.ApiValidationService;
import com.example.restapivalidator.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiValidationController {
    @Autowired
    private ApiValidationService apiValidationService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateModel(@RequestBody Map<String, Object> incomingRequest) {
        return validateModelUtil(incomingRequest);
    }
    public ResponseEntity<?> validateModelUtil(Map<String, Object> incomingRequest) {
        String path = (String) incomingRequest.get("path");
        String method = incomingRequest.get("method").toString().toUpperCase();
        String hashedId = HashUtil.generateHash(method + "-" + path).substring(0, 10);
        // Retrieve the stored API model
        ApiModel apiModel = apiValidationService.findModelById(hashedId).orElse(null);
        if (Objects.isNull(apiModel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API model not found for the given path and method.");
        }
        // Validate headers, query params, and body
        boolean isValid = apiValidationService.validateRequest(incomingRequest, apiModel);
        if (isValid) {
            return ResponseEntity.ok("Request is valid.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request is invalid.");
        }
    }
}

