package com.example.restapivalidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CreationResponseDto {
    private String status;
    private LocalDateTime creationTime;
    private String modelId;
}
