package com.mpdev.reporting.processor;

import com.mpdev.reporting.processor.bytype.ProcessorByTypeFactory;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import com.mpdev.reporting.validation.OutputRecordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class InputItemProcessor implements ItemProcessor<InputItem, OutputItem> {

    private final OutputRecordValidator outputRecordValidator;
    private final ProcessorByTypeFactory processorByTypeFactory;

    public InputItemProcessor(ProcessorByTypeFactory processorByTypeFactory, OutputRecordValidator outputRecordValidator) {
        this.outputRecordValidator = outputRecordValidator;
        this.processorByTypeFactory = processorByTypeFactory;
    }

    @Override
    public OutputItem process(final InputItem input) {

        OutputItem transformedRecord = null;
        final ItemType itemType;
        try {
            itemType = ItemType.valueOf(input.getItemType());
        }
        catch(Exception e) {
            log.error("Input Processor could not parse ItemType [{}]", input.getItemType());
            return null;
        }

        final var processor = processorByTypeFactory.getTypeSpecificProcessor(itemType);
        if (Objects.nonNull(processor)) {
            // tranformation
            transformedRecord = processor.process(input);
            transformedRecord.setItemType(itemType.getAbbreviation());
            log.info("Transformed input {} to {}", input, transformedRecord);
            // validation
            if (!outputRecordValidator.validate(transformedRecord))
                return null;
        }
        return transformedRecord;
    }

}
