package com.mpdev.reporting.validation;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.Jurisdiction;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

import static com.mpdev.reporting.report.ItemType.*;
import static com.mpdev.reporting.validation.ValidationGroups.*;

@Slf4j
@Component
public class OutputRecordValidator {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Set<ConstraintViolation<OutputItem>> validate(OutputItem outputItem) {

        Class<?> validationGroup;
        if (Public.getAbbreviation().equals((outputItem.getItemType())))
            validationGroup = Jurisdiction.US.name().equals(outputItem.getJurisdiction())
                    ? PublicRecordUS.class : PublicRecordNonUS.class;
        else
        if (Confidential.getAbbreviation().equals((outputItem.getItemType())))
            validationGroup = Jurisdiction.US.name().equals(outputItem.getJurisdiction())
                    ? ConfidentialRecordUS.class : ConfidentialRecordNonUS.class;
        else
        if (Secret.getAbbreviation().equals((outputItem.getItemType())))
            validationGroup = Jurisdiction.US.name().equals(outputItem.getJurisdiction())
                    ? SecretRecordUS.class : SecretRecordNonUS.class;
        else
            validationGroup = Default.class;

        Set<ConstraintViolation<OutputItem>> violations = validator.validate(outputItem, validationGroup);
        violations.forEach(violation ->
                log.error("Validation exception: {} - [{}]", violation.getMessage(), violation.getInvalidValue()));
        return violations;
    }
}
