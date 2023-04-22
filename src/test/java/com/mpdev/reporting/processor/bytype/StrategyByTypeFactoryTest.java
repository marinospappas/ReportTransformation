package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.ItemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyByTypeFactoryTest {

    ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));

    private final StrategyByTypeFactory strategyByTypeFactory = new StrategyByTypeFactory(List.of(
            new ConfidentialItemStrategy(reportTranformation),
            new SecretItemStrategy(reportTranformation),
            new PublicItemStrategy(reportTranformation)
    ));

    @Test
    @DisplayName("ProcessorByTypeFactory returns correct processor")
    void testGetProcessorByType() {
        assertTrue(strategyByTypeFactory.getTypeSpecificProcessor(ItemType.Confidential) instanceof ConfidentialItemStrategy);
        assertTrue(strategyByTypeFactory.getTypeSpecificProcessor(ItemType.Secret) instanceof SecretItemStrategy);
        assertTrue(strategyByTypeFactory.getTypeSpecificProcessor(ItemType.Public) instanceof PublicItemStrategy);
        assertNull(strategyByTypeFactory.getTypeSpecificProcessor(ItemType.None));
    }
}
