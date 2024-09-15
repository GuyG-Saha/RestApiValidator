package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResponseDto;
import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.model.Parameter;
import com.example.restapivalidator.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ApiValidationService {
    @Autowired
    private List<ValidationRule> validations;
    @Autowired
    ApiModelService apiModelService;

    public ValidationResponseDto validateRequest(Map<String, Object> incomingRequest) {
        // Extract headers, query params, and body from the incoming request
        String path = (String) incomingRequest.get("path");
        String method = incomingRequest.get("method").toString().toUpperCase();
        String hashedId = HashUtil.generateHash(method + "-" + path).substring(0, 10);
        String status = "200";
        // Retrieve the stored API model
        ApiModel apiModel = apiModelService.findModelById(hashedId).orElse(null);
        if (Objects.isNull(apiModel)) {
            status = "405";
            return new ValidationResponseDto(status, null);
        }
        Map<String, Object> incomingHeaders = (Map<String, Object>) incomingRequest.get("headers");
        Map<String, Object> incomingQueryParams = (Map<String, Object>) incomingRequest.get("queryParams");
        Map<String, Object> incomingBody = (Map<String, Object>) incomingRequest.get("bodyParams");
        List<String> errors = new ArrayList<>();
        if (!validateParams(incomingHeaders, apiModel.getHeaders())) {
            status = "400";
            errors.add("Headers");
        }
        if (!validateParams(incomingQueryParams, apiModel.getQueryParams())) {
            status = "400";
            errors.add("QueryParams");
        }
        if (!validateParams(incomingBody, apiModel.getBodyParams())) {
            status = "400";
            errors.add("Body");
        }
        return new ValidationResponseDto(status, errors);
    }
    private boolean validateParams(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        for (ValidationRule validationRule : validations) {
            if (!validationRule.validate(incomingParams, modelParams))
                return false;
        }
        return true;
    }
}
