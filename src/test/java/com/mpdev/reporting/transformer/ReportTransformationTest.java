package com.mpdev.reporting.transformer;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.processor.bytype.PublicItemProcessor;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.transformation.ReportTransformer;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class ReportTransformationTest {
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();
    private PublicItemProcessor publicItemProcessor;
    private ReportTranformation reportTranformation;
    private Transformer transformer;
    private List<ReportTransformer> reportTransformers;

    List<Class<? extends ReportTransformer>> transformerClasses = List.of(Transformer.class);
    private InputItem inputItem;

    @BeforeEach
    void setup() {
        transformer = mock(Transformer.class);
        reportTransformers = List.of(transformer);
        reportTranformation = new ReportTranformation(reportTransformers);
        publicItemProcessor = new PublicItemProcessor(reportTranformation);
        inputItem = podamFactory.manufacturePojo(InputItem.class);
    }

    @Test
    @DisplayName("Field name in @Transformation annotation must be a valid field in OutputItem")
    void testTransformedFieldNames() {
        var transformationMap = reportTranformation.getTransformationMethodMap();
        var outputFieldsList = Arrays.stream(OutputItem.class.getDeclaredFields()).map(Field::getName).toList();
        transformationMap.forEach((k,v) -> {
            log.info("Field {}", k.getFieldName());
            assertTrue(outputFieldsList.contains(k.getFieldName()),
                    "wrong field name " + k.getFieldName() + " in class " + v.getTransfromer().getClass().getSimpleName());
        });
    }

    @Test
    @DisplayName("Field name supplied in 'transformField' method call must be valid field in OutputItem")
    void testTransformFieldUsesCorrectFieldNames() {
        var outputFieldsList = Arrays.stream(OutputItem.class.getDeclaredFields()).map(Field::getName).toList();
        List<String> transformedFields = new ArrayList<>();
        ReportTranformation reportTranformation1 = mock(ReportTranformation.class);
        when(reportTranformation1.transformField(anyString(), any(InputItem.class)))
                .thenAnswer(invocation -> {
                    transformedFields.add(invocation.getArgument(0, String.class));
                    return RandomStringUtils.randomAlphabetic(10);
                });
        publicItemProcessor = new PublicItemProcessor(reportTranformation1);
        publicItemProcessor.process(inputItem);
        transformedFields.forEach(transformedField ->
            assertTrue(outputFieldsList.contains(transformedField),
                    "wrong field name " + transformedField + " (parameter to transformField call)")

        );
        // also each field must be transformed only once
        var fieldsAppearingMoreThanOnce = transformedFields.stream()
                .filter(field -> Collections.frequency(transformedFields, field) > 1)
                .distinct().toList();
        transformedFields.forEach(field -> assertFalse(fieldsAppearingMoreThanOnce.contains(field),
                field + " is specified as parameter to transform method more than once"));
        assertEquals(0, fieldsAppearingMoreThanOnce.size());
    }
}
