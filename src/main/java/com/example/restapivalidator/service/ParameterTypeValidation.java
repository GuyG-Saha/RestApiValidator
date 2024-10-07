package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
@Component
public class ParameterTypeValidation implements ValidationRule {
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            Object value = incomingParams.get(paramName);
            if (Objects.nonNull(value) && !isTypeMatching(value, paramModel.getType())) {
                resultDto.setValid(false);
                resultDto.getFaultyParams().add(value.toString());
            }
        }
        return resultDto;
    }
    private boolean isTypeMatching(Object value, String expectedType) {
        return ((LinkedHashMap) value).get("type").equals(expectedType.toLowerCase());
    }
}
