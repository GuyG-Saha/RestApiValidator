package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class RequiredFieldConsistencyValidation implements ValidationRule {
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            if (paramModel.isRequired() &&
                    ((LinkedHashMap) incomingParams
                            .get(paramName))
                            .get("required").equals(false)) {
                resultDto.setValid(false);
                resultDto.getFaultyParams().add(paramName);
            }
        }
        return resultDto;
    }
}
