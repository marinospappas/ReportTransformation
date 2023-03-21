package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfidentialItemProcessor implements TypeSpecificProcessor {

    @Override
    public ItemType getProcessorType() {
        return ItemType.Confidential;
    }

    @Override
    public OutputItem process(final InputItem input) {

        log.info("Executing Confidential item processor");
        final String firstName = input.getFirstName().toUpperCase();
        final String lastName = input.getLastName().toUpperCase().substring(0,1);

        return OutputItem.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

}
