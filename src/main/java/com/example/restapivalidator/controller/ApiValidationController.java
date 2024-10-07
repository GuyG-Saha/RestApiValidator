package com.example.restapivalidator.controller;

import com.example.restapivalidator.dto.ValidationResponseDto;
import com.example.restapivalidator.service.ApiModelService;
import com.example.restapivalidator.service.ApiValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/model-validator")
public class ApiValidationController {
    @Autowired
    private ApiValidationService apiValidationService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateModel(@RequestBody Map<String, Object> incomingRequest) {
        return validateModelUtil(incomingRequest);
    }
    public ResponseEntity<?> validateModelUtil(Map<String, Object> incomingRequest) {
        // Validate headers, query params, and body
        ValidationResponseDto validationResult = apiValidationService.validateRequest(incomingRequest);
        return switch (validationResult.getStatus()) {
            case "200" -> ResponseEntity.ok("Request is valid.");
            case "405" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(validationResult);
            case "400" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request is invalid: "
                    + validationResult.getErrors().toString());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(validationResult);
        };
    }
}

