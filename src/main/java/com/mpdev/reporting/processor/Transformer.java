package com.mpdev.reporting.processor;

import com.mpdev.reporting.transformation.ReportTransformer;
import com.mpdev.reporting.transformation.Transformation;
import com.mpdev.reporting.report.inreport.InputItem;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class Transformer implements ReportTransformer {

    @Transformation(fieldName = "contractId")
    public String getContractId(InputItem inputItem) {
        return inputItem.getContractId().toUpperCase();
    }

    @Transformation(fieldName = "firstName")
    public String getFirstName(InputItem inputItem) {
        return inputItem.getFirstName().toUpperCase();
    }

    @Transformation(fieldName = "lastName")
    public String getLastName(InputItem inputItem) {
        return inputItem.getLastName().toUpperCase();
    }

    @Transformation(fieldName = "jurisdiction")
    public String getJurisdiction(InputItem inputItem) {
        return inputItem.getJurisdiction().toUpperCase();
    }

    @Transformation(fieldName = "endDate")
    public String getEndDateUk(InputItem inputItem) {
        LocalDate date = LocalDate.parse(inputItem.getEndDate());
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
