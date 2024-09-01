package com.example.restapivalidator.service;

import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class IncomingParamsNotNullValidation implements ValidationRule {
    @Override
    public boolean validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        return Objects.nonNull(incomingParams);
    }
}
