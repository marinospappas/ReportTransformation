package com.mpdev.reporting.processor.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class TransformerMapData {
    private ReportTransformer transfromer;
    private Method method;
}
