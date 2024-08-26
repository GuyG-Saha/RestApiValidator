package com.example.restapivalidator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter {
    private String type;
    private boolean required;
}
