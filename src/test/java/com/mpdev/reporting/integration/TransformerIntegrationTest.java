package com.mpdev.reporting.integration;

import com.mpdev.reporting.processor.InputItemProcessor;
import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.processor.bytype.ConfidentialItemProcessor;
import com.mpdev.reporting.processor.bytype.ProcessorByTypeFactory;
import com.mpdev.reporting.processor.bytype.PublicItemProcessor;
import com.mpdev.reporting.processor.bytype.SecretItemProcessor;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.validation.OutputRecordValidator;
import com.mpdev.reporting.validation.OutputRecordValidatorTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransformerIntegrationTest {

    private InputItem inputItem;
    private InputItemProcessor inputItemProcessor;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        inputItem.setContractId(RandomStringUtils.randomAlphanumeric(8));
        ProcessorByTypeFactory processorByTypeFactory;
        ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));
        processorByTypeFactory = new ProcessorByTypeFactory(List.of(
                new PublicItemProcessor(reportTranformation), new ConfidentialItemProcessor(reportTranformation), new SecretItemProcessor(reportTranformation)
        ));
        OutputRecordValidator outputRecordValidator = new OutputRecordValidator();
        inputItemProcessor = new InputItemProcessor(processorByTypeFactory, outputRecordValidator);
    }

    @Test
    @DisplayName("Fields for Public item are set correctly")
    void testPublicItem() {
        inputItem.setItemType(ItemType.Public.name());
        var output = inputItemProcessor.process(inputItem);
        assertNotNull(output);
        assertEquals("P", output.getItemType());
        assertEquals(inputItem.getFirstName().toUpperCase(), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase(), output.getLastName());
    }

    @Test
    @DisplayName("Fields for Confidential item are set correctly")
    void testConfidentialItem() {
        inputItem.setItemType(ItemType.Confidential.name());
        var output = inputItemProcessor.process(inputItem);
        assertNotNull(output);
        assertEquals("C", output.getItemType());
        assertEquals(inputItem.getFirstName().toUpperCase(), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase().substring(0,1), output.getLastName());
    }

    @Test
    @DisplayName("Fields for Secret item are set correctly")
    void testSecretItem() {
        inputItem.setItemType(ItemType.Secret.name());
        var output = inputItemProcessor.process(inputItem);
        assertNotNull(output);
        assertEquals("S", output.getItemType());
        assertEquals(inputItem.getFirstName().toUpperCase().substring(0,1), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase().substring(0,1), output.getLastName());
    }
}
