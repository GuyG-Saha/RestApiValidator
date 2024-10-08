package com.example.restapivalidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class ValidationResponseDto {
    private String status;
    private String message;
    private Map<String, List<String>> errors;
}
