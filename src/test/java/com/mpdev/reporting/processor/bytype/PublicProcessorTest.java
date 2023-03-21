package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PublicProcessorTest {

    private InputItem inputItem;
    private PublicItemProcessor publicItemProcessor;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        publicItemProcessor = new PublicItemProcessor();
    }

    @Test
    @DisplayName("Public items fields are transformed correctly")
    void testPublicItemFields() {
        var output = publicItemProcessor.process(inputItem);
        assertEquals(inputItem.getFirstName().toUpperCase(), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase(), output.getLastName());
    }
}
