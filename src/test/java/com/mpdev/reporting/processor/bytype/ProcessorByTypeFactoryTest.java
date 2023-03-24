package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.ItemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorByTypeFactoryTest {

    ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));

    private final ProcessorByTypeFactory processorByTypeFactory = new ProcessorByTypeFactory(List.of(
            new ConfidentialItemProcessor(reportTranformation),
            new SecretItemProcessor(reportTranformation),
            new PublicItemProcessor(reportTranformation)
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
