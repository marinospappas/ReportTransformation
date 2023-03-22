package com.mpdev.reporting.processor.transformation;

import com.mpdev.reporting.report.inreport.InputItem;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Component
public class ReportTranformation {

    private final Map<TransformerMapKey,TransformerMapData> transformationMethodMap;

    public ReportTranformation(List<ReportTransformer> reportTransformerList) {
        Map<TransformerMapKey,TransformerMapData> methodMap = new HashMap<>();
        for (var transformer: reportTransformerList) {
            for (var method: transformer.getClass().getMethods()) {
                if (method.isAnnotationPresent(Transformation.class)) {
                    var fieldName = method.getAnnotation(Transformation.class).fieldName();
                    var contractType = method.getAnnotation(Transformation.class).contractType();
                    for (var jurisdiction: method.getAnnotation(Transformation.class).jurisdiction()) {
                        Method transformationMethod = null;
                        try {
                            transformationMethod = transformer.getClass().getMethod(method.getName(), InputItem.class);
                        }
                        catch (NoSuchMethodException e) {
                            log.error(e.toString());
                        }
                        var mapKey = new TransformerMapKey(fieldName, contractType.toUpperCase(), jurisdiction.toUpperCase());
                        var madData = new TransformerMapData(transformer, transformationMethod);
                        methodMap.put(mapKey, madData);
                    }
                }
            }
        }
        transformationMethodMap = Collections.unmodifiableMap(methodMap);
    }

    public String transformField(String fieldName, InputItem inputItem) {
        var transformationObject = getTransformationObject(fieldName, inputItem);
        if (Objects.isNull(transformationObject))
            return EMPTY;
        try {
            return (String) transformationObject.getMethod().invoke(transformationObject.getTransfromer(), inputItem);
        }
        catch (Exception e) {
            log.debug("transformation implementation could not be invoked for field {} contract type {} jurisdiction {}",
                    fieldName, inputItem.getContractType(), inputItem.getJurisdiction());
            return EMPTY;
        }
    }

    public TransformerMapData getTransformationObject(String fieldName, InputItem inputItem) {
        final String DEF = "*";
        TransformerMapData transformerMapData;
        var contractType = StringUtils.isEmpty(inputItem.getContractType()) ? DEF : inputItem.getContractType().toUpperCase();
        var jurisdiction = StringUtils.isEmpty(inputItem.getJurisdiction()) ? DEF : inputItem.getJurisdiction().toUpperCase();
        if ((transformerMapData = transformationMethodMap.get(new TransformerMapKey(fieldName, DEF, DEF))) != null)
            return transformerMapData;
        if ((transformerMapData = transformationMethodMap.get(new TransformerMapKey(fieldName, contractType, DEF))) != null)
            return transformerMapData;
        if ((transformerMapData = transformationMethodMap.get(new TransformerMapKey(fieldName, DEF, jurisdiction))) != null)
            return transformerMapData;
        if ((transformerMapData = transformationMethodMap.get(new TransformerMapKey(fieldName, contractType, jurisdiction))) != null)
            return transformerMapData;
        log.debug("no transformation method in map for field {} contract type {} jurisdiction {}", fieldName, contractType, jurisdiction);
        return null;
    }
}
