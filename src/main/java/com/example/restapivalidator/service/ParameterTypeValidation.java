package com.example.restapivalidator.service;

import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
@Component
public class ParameterTypeValidation implements ValidationRule {
    @Override
    public boolean validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            Object value = incomingParams.get(paramName);
            if (Objects.nonNull(value) && !isTypeMatching(value, paramModel.getType()))
                return false;
        }
        return true;
    }
    private boolean isTypeMatching(Object value, String expectedType) {
        return ((LinkedHashMap) value).get("type").equals(expectedType.toLowerCase());
    }
}
