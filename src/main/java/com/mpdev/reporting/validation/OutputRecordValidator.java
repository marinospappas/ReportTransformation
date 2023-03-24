package com.mpdev.reporting.validation;

import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
@Component
public class OutputRecordValidator {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public boolean validate(OutputItem outputItem) {
        Set<ConstraintViolation<OutputItem>> violations = validator.validate(outputItem);
        violations.forEach(violation ->
                log.error("Validation exception: {} - [{}]", violation.getMessage(), violation.getInvalidValue()));
        return violations.isEmpty();
    }
}
