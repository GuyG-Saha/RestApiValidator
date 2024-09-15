package com.example.restapivalidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ValidationResponseDto {
    private String status;
    private List<String> errors;
}
