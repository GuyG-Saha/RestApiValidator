package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import com.example.restapivalidator.model.ParameterType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;

import static com.example.restapivalidator.util.DatesUtil.isValidLocalDate;

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
                    if (!isTypeMatching(incomingDataType, expectedType)) {
                        faultyParamsDescription.put("ParameterName", paramName);
                        faultyParamsDescription.put("ExpectedType", expectedType.name().toLowerCase());
                        faultyParamsDescription.put("ActualType", incomingDataType.getClass().getSimpleName());
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
    private boolean isTypeMatchingReflection(Object incomingData, Class<?> expectedType) {
        // Use reflection to check if the types match
        if (expectedType.isAssignableFrom(incomingData.getClass())) {
            return true;
        }
        // Handle collections (lists)
        if (expectedType.isAssignableFrom(List.class) && incomingData instanceof List) {
            List<?> incomingList = (List<?>) incomingData;
            if (!incomingList.isEmpty() && expectedType.getComponentType() != null) {
                return incomingList.stream().allMatch(item -> item.getClass().isAssignableFrom(expectedType.getComponentType()));
            }
        }
        return false;
    }
    private boolean isTypeMatching(Object value, ParameterType expectedType) {
        return switch (expectedType) {
            case STRING -> value instanceof String;
            case INTEGER -> value instanceof Integer;
            case DOUBLE -> value instanceof Double;
            case BOOLEAN -> value instanceof Boolean;
            case LIST -> value instanceof List;
            case MAP -> value instanceof Map;
            case DATE -> isValidLocalDate(value.toString()); // depends on ISO LocalDate successful parsing
            default -> false; // Fallback for unknown types
        };
    }
}
