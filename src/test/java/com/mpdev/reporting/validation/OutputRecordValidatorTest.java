package com.mpdev.reporting.validation;

import com.mpdev.reporting.report.outreport.OutputItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        outputItem.setAgreementNumber("028.145");
        outputRecordValidator = new OutputRecordValidator();
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "null", "''", "A123456", "A12345678", "ABC", "ABCDEFGHIJKLMNOP"
    })
    @DisplayName("Invalid Contract Id raises Exception")
    void testContractIdException(String contractId) {
        outputItem.setContractId(contractId);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX100: Contract Id must be 8 characters long", result.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("Valid Contract Id does not raise Exception")
    void testContractIdNoException() {
        outputItem.setContractId("12345678");
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Empty Last name in Public record raises Exception")
    void testLastNameExceptionPublic(String lastName) {
        outputItem.setLastName(lastName);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX101: Last Name cannot be empty", result.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("Last name value in Public record does not raise Exception")
    void testLastNameNoExceptionPublic() {
        outputItem.setLastName("Lastname");
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "null, C, 123", "'', C, 123", "AB, C, 123", "ABC, C, 123", "ABCDEFGHIJKLMNOP, C, 123",
            "null, S, ''", "'', S, ''", "AB, S, ''", "ABC, S, ''", "ABCDEFGHIJKLMNOP, S, ''",
    })
    @DisplayName("Last name longer than 1 character or Empty in Confidential or Secret record raises Exception")
    void testLastNameExceptionNonPublic(String lastName, String itemType, String agreementNumber) {
        outputItem.setLastName(lastName);
        outputItem.setItemType(itemType);
        outputItem.setAgreementNumber(agreementNumber);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX102: Last Name must only be initial for Confidential or Secret record", result.stream().findFirst().get().getMessage());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "A, C, abc", "B, S, ''"
    })
    @DisplayName("Last name of 1 character in Confidential or Secret record does not raise Exception")
    void testLastNameNoExceptionNonPublic(String lastName, String itemType, String agreementNumber) {
        outputItem.setLastName(lastName);
        outputItem.setItemType(itemType);
        outputItem.setAgreementNumber(agreementNumber);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "125-1, S", "anything, S",
    })
    @DisplayName("Agreement Number present in Secret record raises Exception")
    void testAgreementNumberExceptionSecret(String agreementNumber, String itemType) {
        outputItem.setAgreementNumber(agreementNumber);
        outputItem.setItemType(itemType);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX103: Agreement Number must be empty for Secret record", result.stream().findFirst().get().getMessage());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "'', S", "null, S",
    })
    @DisplayName("Agreement Number empty in Secret record does not raise Exception")
    void testAgreementNumberNoExceptionSecret(String agreementNumber, String itemType) {
        outputItem.setAgreementNumber(agreementNumber);
        outputItem.setItemType(itemType);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "'', C", "null, C",
            "'', P", "null, P",
    })
    @DisplayName("Agreement Number empty in non Secret record raises Exception")
    void testAgreementNumberExceptionNonSecret(String agreementNumber, String itemType) {
        outputItem.setAgreementNumber(agreementNumber);
        outputItem.setItemType(itemType);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(1, result.size());
        assertTrue(result.stream().findFirst().isPresent());
        assertEquals("EX104: Agreement Number must be filled in for non Secret record", result.stream().findFirst().get().getMessage());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
            "abcd, C", "xyz, P",
    })
    @DisplayName("Agreement Number filled in in non Secret record does not raise Exception")
    void testAgreementNumberNoExceptionNonSecret(String agreementNumber, String itemType) {
        outputItem.setAgreementNumber(agreementNumber);
        outputItem.setItemType(itemType);
        var result = outputRecordValidator.validate(outputItem);
        assertEquals(0, result.size());
    }
}
