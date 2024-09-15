package com.example.restapivalidator.service;

import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.model.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ApiValidationService {
    @Autowired
    private List<ValidationRule> validations;

    public boolean validateRequest(Map<String, Object> incomingRequest, ApiModel apiModel) {
        // Extract headers, query params, and body from the incoming request
        Map<String, Object> incomingHeaders = (Map<String, Object>) incomingRequest.get("headers");
        Map<String, Object> incomingQueryParams = (Map<String, Object>) incomingRequest.get("queryParams");
        Map<String, Object> incomingBody = (Map<String, Object>) incomingRequest.get("bodyParams");
        return validateParams(incomingHeaders, apiModel.getHeaders()) &&
                validateParams(incomingQueryParams, apiModel.getQueryParams()) &&
                validateParams(incomingBody, apiModel.getBodyParams());
    }
    private boolean validateParams(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (ValidationRule validationRule : validations) {
            if (!validationRule.validate(incomingParams, modelParams))
                return false;
        }
        return true;
    }
}
