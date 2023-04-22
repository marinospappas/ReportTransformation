package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import com.mpdev.reporting.transformation.ReportTranformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfidentialItemStrategy implements ReportTypeSpecificStrategy {

    public ConfidentialItemStrategy(ReportTranformation reportTranformation) {
        this.reportTranformation = reportTranformation;
    }

    @Override
    public ItemType getReportType() {
        return ItemType.Confidential;
    }

    private final ReportTranformation reportTranformation;

    @Override
    public OutputItem apply(final InputItem input) {

        log.info("Executing Confidential item processor");
        var outputItem = new OutputItem();

        outputItem.setContractId(reportTranformation.transformField("contractId", input));
        outputItem.setFirstName(reportTranformation.transformField("firstName", input));
        outputItem.setLastName(reportTranformation.transformField("lastName", input).substring(0,1));
        outputItem.setJurisdiction(reportTranformation.transformField("jurisdiction", input));
        outputItem.setEndDate(reportTranformation.transformField("endDate", input));

        return outputItem;
    }

}
