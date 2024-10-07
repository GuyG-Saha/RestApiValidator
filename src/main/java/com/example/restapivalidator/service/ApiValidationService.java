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
    private Map<String, String> detectedWrongParams;
    @Autowired
    private List<ValidationRule> validations;
    @Autowired
    ApiModelService apiModelService;

    public ApiValidationService() {
        detectedWrongParams = new HashMap<String, String>();
    }

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
            return new ValidationResponseDto(status, "Cannot find a matching model", null);
        }
        Map<String, Object> incomingHeaders = (Map<String, Object>) incomingRequest.get("headers");
        Map<String, Object> incomingQueryParams = (Map<String, Object>) incomingRequest.get("queryParams");
        Map<String, Object> incomingBody = (Map<String, Object>) incomingRequest.get("bodyParams");
        List<String> errors = new ArrayList<>();
        ValidationResultDto result = validateParams(incomingHeaders, apiModel.getHeaders());
        if (!result.isValid()) {
            status = "400";
            errors.add("Headers: ");
            errors.add(result.getFaultyParams().toString());
            return new ValidationResponseDto(status, "Error Occurred", errors);
        }
        result = validateParams(incomingQueryParams, apiModel.getQueryParams());
        if (!result.isValid()) {
            status = "400";
            errors.add("QueryParams: ");
            errors.add(result.getFaultyParams().toString());
            return new ValidationResponseDto(status, "Error Occurred", errors);
        }
        result = validateParams(incomingBody, apiModel.getBodyParams());
        if (!result.isValid()) {
            status = "400";
            errors.add("BodyParams: ");
            errors.add(result.getFaultyParams().toString());
            return new ValidationResponseDto(status, "Error Occurred", errors);
        }
        return new ValidationResponseDto(status, "No Errors", errors);
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
