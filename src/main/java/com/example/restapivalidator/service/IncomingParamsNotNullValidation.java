package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResultDto;
import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class IncomingParamsNotNullValidation implements ValidationRule {
    private final Map<String, String> faultyParamsDescription = new LinkedHashMap<>();
    @Override
    public ValidationResultDto validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto resultDto = new ValidationResultDto();
        if (Objects.isNull(incomingParams)) {
            resultDto.setValid(false);
        }
        else {
            for (String fieldName : incomingParams.keySet()) {
                if (modelParams.containsKey(fieldName)) {
                    if (Objects.isNull(incomingParams.get(fieldName)) && modelParams.get(fieldName).isRequired()) {
                        resultDto.setValid(false);
                        faultyParamsDescription.put("ParameterName", fieldName);
                        faultyParamsDescription.put("RequiredValue", null);
                        resultDto.getFaultyParams().add(faultyParamsDescription.toString());
                    }
                }
            }
        }
        return resultDto;
    }
}
