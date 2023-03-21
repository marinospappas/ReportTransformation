package com.mpdev.reporting.integration;

import com.mpdev.reporting.processor.InputItemProcessor;
import com.mpdev.reporting.processor.bytype.ConfidentialItemProcessor;
import com.mpdev.reporting.processor.bytype.ProcessorByTypeFactory;
import com.mpdev.reporting.processor.bytype.PublicItemProcessor;
import com.mpdev.reporting.processor.bytype.SecretItemProcessor;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;


public class TransformerIntegrationTest {

    private InputItem inputItem;
    private InputItemProcessor inputItemProcessor;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        ProcessorByTypeFactory processorByTypeFactory;
        processorByTypeFactory = new ProcessorByTypeFactory(List.of(
                new PublicItemProcessor(), new ConfidentialItemProcessor(), new SecretItemProcessor()
        ));
        inputItemProcessor = new InputItemProcessor(processorByTypeFactory);
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
