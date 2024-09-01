package com.example.restapivalidator.service;

import com.example.restapivalidator.model.Parameter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class RequiredFieldConsistencyValidation implements ValidationRule {
    @Override
    public boolean validate(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            if (paramModel.isRequired() &&
                    ((LinkedHashMap) incomingParams
                            .get(paramName))
                            .get("required").equals(false)) {
                return false;
            }
        }
        return true;
    }
}
