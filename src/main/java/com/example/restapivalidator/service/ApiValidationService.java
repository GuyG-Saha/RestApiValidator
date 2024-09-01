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
}
