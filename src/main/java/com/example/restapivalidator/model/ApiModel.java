package com.example.restapivalidator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@Document(collection = "ApiModels")
public class ApiModel {
    @org.springframework.data.annotation.Id
    private String id;
    private String path;
    private String method;
    private Map<String, Parameter> queryParams;
    private Map<String, Parameter> headers;
    private Map<String, Parameter> bodyParams;
}
