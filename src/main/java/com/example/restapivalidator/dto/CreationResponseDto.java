package com.example.restapivalidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CreationResponseDto {
    private String status;
    private String errorMessage;
    private LocalDateTime creationTime;
    private String modelId;
}
