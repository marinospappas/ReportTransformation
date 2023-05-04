package com.mpdev.reporting.processor;

import com.mpdev.reporting.processor.bytype.StrategyByTypeFactory;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import com.mpdev.reporting.validation.OutputRecordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class InputItemProcessor implements ItemProcessor<InputItem, OutputItem> {

    private final OutputRecordValidator outputRecordValidator;
    private final StrategyByTypeFactory strategyByTypeFactory;

    public InputItemProcessor(StrategyByTypeFactory strategyByTypeFactory, OutputRecordValidator outputRecordValidator) {
        this.outputRecordValidator = outputRecordValidator;
        this.strategyByTypeFactory = strategyByTypeFactory;
    }

    @Override
    public OutputItem process(@NonNull InputItem input) {

        OutputItem transformedRecord = null;
        final ItemType itemType;
        try {
            itemType = ItemType.valueOf(input.getItemType());
        }
        catch(Exception e) {
            log.error("Input Processor could not parse ItemType [{}]", input.getItemType());
            return null;
        }

        final var strategy = strategyByTypeFactory.getTypeSpecificProcessor(itemType);
        if (Objects.nonNull(strategy)) {
            // transformation
            transformedRecord = strategy.apply(input);
            transformedRecord.setItemType(itemType.getAbbreviation());
            log.info("Transformed input {} to {}", input, transformedRecord);
            // validation
            var validationResults = outputRecordValidator.validate(transformedRecord);
            if (validationResults.size() > 0)
                return null;
        }
        return transformedRecord;
    }

}
