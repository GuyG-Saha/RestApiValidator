package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import com.example.restapivalidator.model.ParameterType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
@Component
@Slf4j
public class ParameterTypeValidation implements ValidationRule {
    private final Map<String, String> faultyParamsDescription = new LinkedHashMap<>();
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            Object incomingDataType = incomingParams.get(paramName);
            if (Objects.nonNull(incomingDataType)) {
                try {
                    ParameterType expectedType = paramModel.getType();
                    String actualType = extractActualType(incomingDataType);
                    if (!isTypeMatching(actualType, expectedType)) {
                        faultyParamsDescription.put("ParameterName", paramName);
                        faultyParamsDescription.put("ExpectedType", expectedType.name().toLowerCase());
                        faultyParamsDescription.put("ActualType", actualType.toLowerCase());
                        resultDto.setValid(false);
                        resultDto.getFaultyParams().add(faultyParamsDescription.toString());
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Invalid parameter type for parameter: {}", paramName);
                    resultDto.setValid(false);
                    resultDto.getFaultyParams().add(
                            String.format("Invalid type definition for parameter: %s", paramName)
                    );
                }
            }
        }
        return resultDto;
    }
    private String extractActualType(Object value) {
        if (value instanceof Map<?, ?> typeInfo) {
            return typeInfo.get("type").toString();
        }
        throw new IllegalArgumentException("Invalid parameter structure");
    }
    private boolean isTypeMatching(String actualType, ParameterType expectedType) {
        try {
            ParameterType actual = ParameterType.fromString(actualType);
            return actual == expectedType;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
