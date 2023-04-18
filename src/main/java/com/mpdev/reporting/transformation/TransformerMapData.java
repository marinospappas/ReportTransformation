package com.mpdev.reporting.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class TransformerMapData {
    private ReportTransformer transformer;
    private Method method;
}
