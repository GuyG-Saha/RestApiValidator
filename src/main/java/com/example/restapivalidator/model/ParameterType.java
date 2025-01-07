package com.example.restapivalidator.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ParameterType {
    STRING, INTEGER, DOUBLE, BOOLEAN, FLOAT, OBJECT, ARRAY, DATE;
    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
    public static ParameterType fromString(String type) {
        return valueOf(type.toUpperCase());
    }
}
