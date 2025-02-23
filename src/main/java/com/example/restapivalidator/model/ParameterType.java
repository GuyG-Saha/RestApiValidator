package com.example.restapivalidator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private static final Map<String, ParameterType> STRING_TO_ENUM = new HashMap<>();

    static {
        for (ParameterType type : values()) {
            STRING_TO_ENUM.put(type.name().toLowerCase(Locale.ROOT), type);
        }
    }

    ParameterType(Class<?> clazz) {
        this.clazz = clazz;
    }
    public Class<?> getClazz() {
        return clazz;
    }
    public static Map<String, ParameterType> getStringToEnum() {
        return STRING_TO_ENUM;
    }
    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static ParameterType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("ParameterType cannot be null");
        }
        ParameterType paramType = STRING_TO_ENUM.get(type.toLowerCase(Locale.ROOT));
        if (paramType == null) {
            throw new IllegalArgumentException("Invalid ParameterType: " + type);
        }
        return paramType;
    }
}
