package com.mpdev.reporting.processor;

import com.mpdev.reporting.inreport.InputItem;
import com.mpdev.reporting.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class InputItemProcessor implements ItemProcessor<InputItem, OutputItem> {

    @Override
    public OutputItem process(final InputItem input) {
        final String firstName = input.getFirstName().toUpperCase();
        final String lastName = input.getLastName().toUpperCase();

        final OutputItem transformedRecord = new OutputItem(firstName, lastName);

        log.info("Transformed input {} to {}", input, transformedRecord);

        return transformedRecord;
    }

}
