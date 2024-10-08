package com.example.restapivalidator.service;

import com.example.restapivalidator.dto.ValidationResponseDto;
import com.example.restapivalidator.dto.ValidationResultDto;
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

    public ApiValidationService() {

    }
    public ValidationResponseDto validateRequest(Map<String, Object> incomingRequest) {
        Map<String, List<String>> errorMap = new HashMap<>();
        // Extract path and method from the incoming request
        String path = (String) incomingRequest.get("path");
        String method = incomingRequest.get("method").toString().toUpperCase();
        String hashedId = HashUtil.generateHash(method + "-" + path).substring(0, 10);
        String status = "200";
        // Retrieve the stored API model
        ApiModel apiModel = apiModelService.findModelById(hashedId).orElse(null);
        if (Objects.isNull(apiModel)) {
            status = "405";
            return new ValidationResponseDto(status, "Cannot find a matching model", null);
        }
        // Extract headers, query params, and body from the incoming request
        Map<String, Object> incomingHeaders = (Map<String, Object>) incomingRequest.get("headers");
        Map<String, Object> incomingQueryParams = (Map<String, Object>) incomingRequest.get("queryParams");
        Map<String, Object> incomingBody = (Map<String, Object>) incomingRequest.get("bodyParams");
        // Validate each part of the request and collect errors
        validateAndCollectErrors("Headers", incomingHeaders, apiModel.getHeaders(), errorMap);
        validateAndCollectErrors("QueryParams", incomingQueryParams, apiModel.getQueryParams(), errorMap);
        validateAndCollectErrors("BodyParams", incomingBody, apiModel.getBodyParams(), errorMap);
        if (!errorMap.isEmpty()) {
            status = "400";
            return new ValidationResponseDto(status, "Error Occurred", errorMap);
        }
        return new ValidationResponseDto(status, "No Errors", null);
    }
    private void validateAndCollectErrors(String section, Map<String, Object> incomingParams,
                                          Map<String, Parameter> expectedParams,
                                          Map<String, List<String>> errorMap) {
        ValidationResultDto result = validateParams(incomingParams, expectedParams);
        if (!result.isValid()) {
            errorMap.computeIfAbsent(section, k -> new ArrayList<>()).addAll(result.getFaultyParams());
        }
    }
    private ValidationResultDto validateParams(Map<String, Object> incomingParams, Map<String, Parameter> modelParams) {
        ValidationResultDto result = new ValidationResultDto();
        HashSet<String> params = new HashSet<>();
        for (ValidationRule validationRule : validations) {
            ValidationResultDto resultDto = validationRule.validate(incomingParams, modelParams);
            if (!resultDto.isValid()) {
                params.addAll(resultDto.getFaultyParams());
                if (result.isValid())
                    result.setValid(false);
                result.setFaultyParams(params);
            }
        }
        return result;
    }
}
