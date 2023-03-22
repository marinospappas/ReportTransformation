package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.inreport.InputItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PublicProcessorTest {

    private InputItem inputItem;
    private PublicItemProcessor publicItemProcessor;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));
        publicItemProcessor = new PublicItemProcessor(reportTranformation);
    }

    @Test
    @DisplayName("Public items fields are transformed correctly")
    void testPublicItemFields() {
        var output = publicItemProcessor.process(inputItem);
        assertEquals(inputItem.getFirstName().toUpperCase(), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase(), output.getLastName());
    }
}
