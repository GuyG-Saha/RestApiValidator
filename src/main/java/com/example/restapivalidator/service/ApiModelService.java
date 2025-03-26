package com.example.restapivalidator.service;

import com.example.restapivalidator.model.*;
import com.example.restapivalidator.repository.ApiSchemaRepository;
import com.example.restapivalidator.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.restapivalidator.util.HashUtil;

@Service
public class ApiModelService {
    @Autowired
    ApiSchemaRepository apiModelRepository;
    private static final Set<HttpMethod> ALLOWED_METHODS = Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE);

    public void saveModel(ApiModel apiModel) throws JsonProcessingException {
        validatePath(apiModel.getPath());
        validateMethod(apiModel.getMethod());

        String jsonSchemaHeaders = new ObjectMapper().writeValueAsString(apiModel.getHeaders());
        JsonUtil.validateJsonSchemaDepth(jsonSchemaHeaders);
        validateParameters(apiModel.getHeaders());

        String jsonSchemaQueryParams = new ObjectMapper().writeValueAsString(apiModel.getQueryParams());
        JsonUtil.validateJsonSchemaDepth(jsonSchemaQueryParams);
        validateParameters(apiModel.getQueryParams());

        String jsonSchemaBodyParams = new ObjectMapper().writeValueAsString(apiModel.getBodyParams());
        JsonUtil.validateJsonSchemaDepth(jsonSchemaBodyParams);
        validateParameters(apiModel.getBodyParams());

        String apiModelId = HashUtil.generateHash(apiModel.getMethod().toUpperCase() +
                "-" + apiModel.getPath()).substring(0, 10);
        apiModel.setId(apiModelId);
        apiModelRepository.save(apiModel);
    }
    public Optional<ApiModel> findModelById(String id) {
        return apiModelRepository.findById(id);
    }

    public List<ApiModel> findAll() {
        return apiModelRepository.findAll();
    }

    private void validatePath(String path) {
        if (path == null || !path.matches("^/[a-zA-Z0-9_/{}/-]+$")) {
            throw new IllegalArgumentException("Invalid API path: " + path);
        }
    }
    private void validateMethod(String method) {
        try {
            HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
            if (!ALLOWED_METHODS.contains(httpMethod)) {
                throw new IllegalArgumentException("Method not allowed: " + method);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }
    private void validateParameters(Map<String, Parameter> parameters) {
        if (Objects.nonNull(parameters)) {
            for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
                String paramName = entry.getKey();
                Parameter param = entry.getValue();
                if (!paramName.matches("^[a-zA-Z0-9_-]+$")) {
                    throw new IllegalArgumentException("Invalid parameter name: " + paramName);
                }
                if (param.getType() == null) {
                    throw new IllegalArgumentException("Missing type for parameter: " + paramName);
                }
                if (!ParameterType.getStringToEnum().containsKey(param.getType().toLowerCase())) {
                    throw new IllegalArgumentException("Invalid ParameterType: " + param.getType());
                }
            }
        }
    }

}
