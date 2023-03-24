package com.mpdev.reporting.validation;

import com.mpdev.reporting.processor.InputItemProcessor;
import com.mpdev.reporting.processor.Transformer;
import com.mpdev.reporting.processor.bytype.ConfidentialItemProcessor;
import com.mpdev.reporting.processor.bytype.ProcessorByTypeFactory;
import com.mpdev.reporting.processor.bytype.PublicItemProcessor;
import com.mpdev.reporting.processor.bytype.SecretItemProcessor;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutputRecordValidatorTest {

    private OutputItem outputItem;
    private OutputRecordValidator outputRecordValidator = new OutputRecordValidator();
    private final PodamFactoryImpl podamFactory = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        outputItem = podamFactory.manufacturePojo(OutputItem.class);
        outputItem.setContractId(RandomStringUtils.randomAlphanumeric(8));
        outputItem.setJurisdiction("EU");
        outputItem.setItemType("P");
        outputRecordValidator = new OutputRecordValidator();
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "null", "''", "A123456", "A12345678", "ABC", "ABCDEFGHIJKLMNOP"
    })
    void testContractIdException(String contractId) {
        outputItem.setContractId(contractId);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX100: Contract Id must be 8 characters long", result.stream().findFirst().get().getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testLastNameExceptionPublic(String lastName) {
        outputItem.setLastName(lastName);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX101: Last Name cannot be empty", result.stream().findFirst().get().getMessage());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "null, C", "'', C", "AB, C", "ABC, C", "ABCDEFGHIJKLMNOP, C",
    })
    void testLastNameExceptionNonPublic(String lastName, String itemType) {
        outputItem.setLastName(lastName);
        outputItem.setItemType(itemType);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX102: Last Name must only be initial for Confidential or Secret record", result.stream().findFirst().get().getMessage());
    }
}
