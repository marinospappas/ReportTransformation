package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcessorByTypeFactory {

    private final Map<ItemType,TypeSpecificProcessor> processorMap;

    public ProcessorByTypeFactory(List<TypeSpecificProcessor> typeSpecificProcessors) {
        Map<ItemType,TypeSpecificProcessor> tempProcessorMap = new HashMap<>();
        for (var processor: typeSpecificProcessors)
            tempProcessorMap.put(processor.getProcessorType(), processor);
        processorMap = Collections.unmodifiableMap(tempProcessorMap);
    }

    public TypeSpecificProcessor getTypeSpecificProcessor(ItemType itemType) {
        return processorMap.get(itemType);
    }
}
