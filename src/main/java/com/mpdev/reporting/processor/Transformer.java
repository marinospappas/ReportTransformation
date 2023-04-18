package com.mpdev.reporting.processor;

import com.mpdev.reporting.transformation.ReportTransformer;
import com.mpdev.reporting.transformation.Transformation;
import com.mpdev.reporting.report.inreport.InputItem;
import org.springframework.stereotype.Component;

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
    public String getEndDate(InputItem inputItem) {
        return inputItem.getEndDate();
    }
}
