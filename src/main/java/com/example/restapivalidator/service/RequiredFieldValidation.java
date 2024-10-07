package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequiredFieldValidation implements ValidationRule {
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            if (paramModel.isRequired() && !incomingParams.containsKey(paramName)) {
                resultDto.setValid(false);
                resultDto.getFaultyParams().add(paramName);
            }
        }
        return resultDto;
    }
}

