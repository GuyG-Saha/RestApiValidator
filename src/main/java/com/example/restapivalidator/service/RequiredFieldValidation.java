package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RequiredFieldValidation implements ValidationRule {
    private final Map<String, String> faultyParamsDescription = new LinkedHashMap<>();
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            if (paramModel.isRequired() && !incomingParams.containsKey(paramName)) {
                resultDto.setValid(false);
                faultyParamsDescription.put("ParameterName", paramName);
                faultyParamsDescription.put("RequiredField", "Missing");
                resultDto.getFaultyParams().add(faultyParamsDescription.toString());
            }
        }
        return resultDto;
    }
}

