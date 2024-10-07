package com.example.restapivalidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
public class ValidationResultDto {
    private boolean valid;
    private Set<String> faultyParams;

    public ValidationResultDto() {
        valid = true;
        faultyParams = new HashSet<>();
    }
}
