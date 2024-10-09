package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
@Component
public class ParameterTypeValidation implements ValidationRule {
    private final Map<String, String> faultyParamsDescription = new LinkedHashMap<>();
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            Object incomingDataType = incomingParams.get(paramName);
            if (Objects.nonNull(incomingDataType) && !isTypeMatching(incomingDataType, paramModel.getType())) {
                faultyParamsDescription.put("ParameterName", paramName);
                faultyParamsDescription.put("ExpectedType", paramModel.getType());
                faultyParamsDescription.put("ActualType", ((LinkedHashMap<?, ?>) incomingDataType).get("type").toString());
                resultDto.setValid(false);
                resultDto.getFaultyParams().add(faultyParamsDescription.toString());
            }
        }
        return resultDto;
    }
    private boolean isTypeMatching(Object value, String expectedType) {
        return ((LinkedHashMap<?, ?>) value).get("type").equals(expectedType.toLowerCase());
    }
}
