package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategyByTypeFactory {

    private final Map<ItemType, ReportTypeSpecificStrategy> processorMap;

    public StrategyByTypeFactory(List<ReportTypeSpecificStrategy> reportTypeSpecificStrategies) {
        Map<ItemType, ReportTypeSpecificStrategy> tempProcessorMap = new HashMap<>();
        for (var processor: reportTypeSpecificStrategies)
            tempProcessorMap.put(processor.getReportType(), processor);
        processorMap = Collections.unmodifiableMap(tempProcessorMap);
    }

    public ReportTypeSpecificStrategy getTypeSpecificProcessor(ItemType itemType) {
        return processorMap.get(itemType);
    }
}
