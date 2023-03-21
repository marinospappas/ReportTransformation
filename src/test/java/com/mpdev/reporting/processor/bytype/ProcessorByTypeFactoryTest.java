package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorByTypeFactoryTest {

    private final ProcessorByTypeFactory processorByTypeFactory = new ProcessorByTypeFactory(List.of(
            new ConfidentialItemProcessor(),
            new SecretItemProcessor(),
            new PublicItemProcessor()
    ));

    @Test
    @DisplayName("ProcessorByTypeFactory returns correct processor")
    void testGetProcessorByType() {
        assertTrue(processorByTypeFactory.getTypeSpecificProcessor(ItemType.Confidential) instanceof ConfidentialItemProcessor);
        assertTrue(processorByTypeFactory.getTypeSpecificProcessor(ItemType.Secret) instanceof SecretItemProcessor);
        assertTrue(processorByTypeFactory.getTypeSpecificProcessor(ItemType.Public) instanceof PublicItemProcessor);
        assertNull(processorByTypeFactory.getTypeSpecificProcessor(ItemType.None));
    }
}
