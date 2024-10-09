package com.example.restapivalidator.controller;

import com.example.restapivalidator.dto.ValidationResponseDto;
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
        ValidationResponseDto validationResult = apiValidationService.validateRequest(incomingRequest);
        return switch (validationResult.getStatus()) {
            case "200" -> {
                validationResult.setMessage("Request is valid");
                yield ResponseEntity.ok(validationResult);
            }
            case "405" -> {
                validationResult.setMessage("API model not found, please check HTTP method and path");
                yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(validationResult); }
            case "400" -> {
                validationResult.setMessage("Request is invalid");
                yield ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
            }
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(validationResult);
        };
    }
}

