package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.transformation.ReportTranformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SecretProcessorTest {

    private InputItem inputItem;
    private SecretItemStrategy secretItemStrategy;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));
        secretItemStrategy = new SecretItemStrategy(reportTranformation);
    }

    @Test
    @DisplayName("Public items fields are transformed correctly")
    void testPublicItemFields() {
        var output = secretItemStrategy.apply(inputItem);
        assertEquals(inputItem.getFirstName().toUpperCase().substring(0,1), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase().substring(0,1), output.getLastName());
        assertEquals(inputItem.getJurisdiction().toUpperCase(), output.getJurisdiction());
    }
}
