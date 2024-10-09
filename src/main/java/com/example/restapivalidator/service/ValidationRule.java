package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;

import java.util.Map;

public interface ValidationRule {
    ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams);
}
