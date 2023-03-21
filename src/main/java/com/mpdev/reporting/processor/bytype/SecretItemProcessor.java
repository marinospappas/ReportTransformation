package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecretItemProcessor implements TypeSpecificProcessor {

    @Override
    public ItemType getProcessorType() {
        return ItemType.Secret;
    }

    @Override
    public OutputItem process(final InputItem input) {

        log.info("Executing Secret item processor");

        final String firstName = input.getFirstName().toUpperCase().substring(0,1);
        final String lastName = input.getLastName().toUpperCase().substring(0,1);

        return OutputItem.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

}
