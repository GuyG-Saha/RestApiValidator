package com.example.restapivalidator.service;

import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.model.Parameter;
import com.example.restapivalidator.repository.ApiSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ApiValidationService {
    @Autowired
    private ApiSchemaRepository apiModelRepository;
    @Autowired
    private List<ValidationRule> validations;
    public Optional<ApiModel> findModelById(String id) {
        return apiModelRepository.findById(id);
    }
    public boolean validateRequest(Map<String, Object> incomingRequest, ApiModel apiModel) {
        // Extract headers, query params, and body from the incoming request
        Map<String, Object> incomingHeaders = (Map<String, Object>) incomingRequest.get("headers");
        Map<String, Object> incomingQueryParams = (Map<String, Object>) incomingRequest.get("queryParams");
        Map<String, Object> incomingBody = (Map<String, Object>) incomingRequest.get("bodyParams");
        return validateParamsNew(incomingHeaders, apiModel.getHeaders()) &&
                validateParamsNew(incomingQueryParams, apiModel.getQueryParams()) &&
                validateParamsNew(incomingBody, apiModel.getBodyParams());
    }
    private boolean validateParamsNew(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (ValidationRule validationRule : validations) {
            if (!validationRule.validate(incomingParams, modelParams))
                return false;
        }
        return true;
    }
    private boolean validateParams(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (Map.Entry<String, Parameter> entry : modelParams.entrySet()) {
            String paramName = entry.getKey();
            Parameter paramModel = entry.getValue();
            if (Objects.isNull(incomingParams)) {
                return false;
            }
            if (paramModel.isRequired() && !incomingParams.containsKey(paramName)) {
                return false;
            }
            if (paramModel.isRequired() &&
                    ((LinkedHashMap) incomingParams
                            .get(paramName))
                            .get("required").equals(false)) {
                return false;
            }
            // If the parameter is present, check its type
            Object value = incomingParams.get(paramName);
            if (value != null && !isTypeMatching(value, paramModel.getType())) {
                return false;
            }
        }
        return true;
    }
    private boolean isTypeMatching(Object value, String expectedType) {
        return ((LinkedHashMap) value).get("type").equals(expectedType.toLowerCase());
    }

}
