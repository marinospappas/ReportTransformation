package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublicItemProcessor implements TypeSpecificProcessor {

    @Override
    public ItemType getProcessorType() {
        return ItemType.Public;
    }

    @Override
    public OutputItem process(final InputItem input) {

        log.info("Executing Public item processor");

        final String firstName = input.getFirstName().toUpperCase();
        final String lastName = input.getLastName().toUpperCase();

        return OutputItem.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

}
