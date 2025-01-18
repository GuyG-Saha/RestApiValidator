package com.example.restapivalidator.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Map;

public enum ParameterType {
    STRING(String.class),
    INTEGER(Integer.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    LIST(List.class),
    MAP(Map.class),
    DATE(java.time.LocalDate.class);

    private final Class<?> clazz;

    ParameterType(Class<?> clazz) {
        this.clazz = clazz;
    }
    public Class<?> getClazz() {
        return clazz;
    }
    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
    public static ParameterType fromString(String type) {
        return valueOf(type.toUpperCase());
    }
}
