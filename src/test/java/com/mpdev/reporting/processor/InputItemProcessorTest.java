package com.mpdev.reporting.processor;

import com.mpdev.reporting.processor.bytype.*;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import com.mpdev.reporting.validation.OutputRecordValidator;
import com.mpdev.reporting.validation.OutputRecordValidatorTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class InputItemProcessorTest {

    private InputItem inputItem;
    private InputItemProcessor inputItemProcessor;
    @Mock
    private SecretItemProcessor secretItemProcessor;
    @Mock
    private PublicItemProcessor publicItemProcessor;
    @Mock
    private ConfidentialItemProcessor confidentialItemProcessor;
    @Mock
    private OutputRecordValidator outputRecordValidator;
    @Mock
    ConstraintViolation<OutputItem> constraintViolation;
    private AutoCloseable closeable;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(publicItemProcessor.process(any(InputItem.class))).thenReturn(new OutputItem());
        when(publicItemProcessor.getProcessorType()).thenReturn(ItemType.Public);
        when(confidentialItemProcessor.process(any(InputItem.class))).thenReturn(new OutputItem());
        when(confidentialItemProcessor.getProcessorType()).thenReturn(ItemType.Confidential);
        when(secretItemProcessor.process(any(InputItem.class))).thenReturn(new OutputItem());
        when(secretItemProcessor.getProcessorType()).thenReturn(ItemType.Secret);
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        ProcessorByTypeFactory processorByTypeFactory;
        processorByTypeFactory = new ProcessorByTypeFactory(List.of(
                secretItemProcessor, confidentialItemProcessor, publicItemProcessor
        ));
        inputItemProcessor = new InputItemProcessor(processorByTypeFactory, outputRecordValidator);
    }

    @AfterEach
    void cleanup() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("InputItemProcessor uses PublicItemProcessor and sets ItemType to P for Public Type")
    void testPublicProcessorInvocation() {
        when(outputRecordValidator.validate(any(OutputItem.class))).thenReturn(Set.of());
        inputItem.setItemType(ItemType.Public.name());
        var output = inputItemProcessor.process(inputItem);
        verify(publicItemProcessor).process(any(InputItem.class));
        assertNotNull(output);
        assertEquals("P", output.getItemType());
        verify(secretItemProcessor, never()).process(any(InputItem.class));
        verify(confidentialItemProcessor, never()).process(any(InputItem.class));
    }

    @Test
    @DisplayName("InputItemProcessor uses SecretItemProcessor and sets ItemType to S for Secret Type")
    void testSecretProcessorInvocation() {
        when(outputRecordValidator.validate(any(OutputItem.class))).thenReturn(Set.of());
        inputItem.setItemType(ItemType.Secret.name());
        var output = inputItemProcessor.process(inputItem);
        verify(secretItemProcessor).process(any(InputItem.class));
        assertNotNull(output);
        assertEquals("S", output.getItemType());
        verify(publicItemProcessor, never()).process(any(InputItem.class));
        verify(confidentialItemProcessor, never()).process(any(InputItem.class));
    }

    @Test
    @DisplayName("InputItemProcessor uses ConfidentialItemProcessor and sets ItemType to C for Confidential Type")
    void testConfidentialProcessorInvocation() {
        when(outputRecordValidator.validate(any(OutputItem.class))).thenReturn(Set.of());
        inputItem.setItemType(ItemType.Confidential.name());
        var output = inputItemProcessor.process(inputItem);
        verify(confidentialItemProcessor).process(any(InputItem.class));
        assertNotNull(output);
        assertEquals("C", output.getItemType());
        verify(secretItemProcessor, never()).process(any(InputItem.class));
        verify(publicItemProcessor, never()).process(any(InputItem.class));
    }

    @ParameterizedTest
    @DisplayName("When InputType is invalid then no Output is produced")
    @CsvSource(nullValues = "n/a", value = {"n/a", "''", "xxxxxxx", "1234"})
    void testInvalidInputType(String inputType) {
        when(outputRecordValidator.validate(any(OutputItem.class))).thenReturn(Set.of());
        inputItem.setItemType(inputType);
        var output = inputItemProcessor.process(inputItem);
        assertNull(output);
    }

    @Test
    @DisplayName("When output item validation fails then no Output is produced")
    void testValidationException() {
        when(outputRecordValidator.validate(any(OutputItem.class))).thenReturn(Set.of(constraintViolation));
        var output = inputItemProcessor.process(inputItem);
        assertNull(output);
    }
}
