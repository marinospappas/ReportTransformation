package com.mpdev.reporting.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransformerMapKey {
    private String fieldName;
    private String contractType;
    private String jurisdiction;
}
