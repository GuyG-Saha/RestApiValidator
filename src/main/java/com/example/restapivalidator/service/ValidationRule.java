package com.example.restapivalidator.service;

import com.example.restapivalidator.model.Parameter;

import java.util.Map;

public interface ValidationRule {
    boolean validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams);
}
