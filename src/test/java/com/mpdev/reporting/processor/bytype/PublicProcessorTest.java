package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.inreport.InputItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PublicProcessorTest {

    private InputItem inputItem;
    private PublicItemStrategy publicItemStrategy;
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        inputItem = podamFactory.manufacturePojo(InputItem.class);
        ReportTranformation reportTranformation = new ReportTranformation(List.of(new Transformer()));
        publicItemStrategy = new PublicItemStrategy(reportTranformation);
    }

    @Test
    @DisplayName("Public items fields are transformed correctly")
    void testPublicItemFields() {
        var output = publicItemStrategy.apply(inputItem);
        assertEquals(inputItem.getFirstName().toUpperCase(), output.getFirstName());
        assertEquals(inputItem.getLastName().toUpperCase(), output.getLastName());
        assertEquals(inputItem.getJurisdiction().toUpperCase(), output.getJurisdiction());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "2024-09-01, UK, 01/09/2024",
            "2024-09-01, EU, 01/09/2024",
    })
    @DisplayName("EndDate field is enriched according to Jurisdiction")
    void testEndDate(String endDate, String jurisdiction, String expected) {
        inputItem.setEndDate(endDate);
        inputItem.setJurisdiction(jurisdiction);
        var output = publicItemStrategy.apply(inputItem);
        assertEquals(expected, output.getEndDate());
    }

}
