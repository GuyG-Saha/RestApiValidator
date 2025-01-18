package com.example.restapivalidator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter {
    private ParameterType type;
    private boolean required;
}
