package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.transformation.ReportTranformation;
import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublicItemProcessor implements TypeSpecificProcessor {

    private final ReportTranformation reportTranformation;

    public PublicItemProcessor(ReportTranformation reportTranformation) {
        this.reportTranformation = reportTranformation;
    }

    @Override
    public ItemType getProcessorType() {
        return ItemType.Public;
    }

    @Override
    public OutputItem process(final InputItem input) {

        log.info("Executing Public item processor");
        var outputItem = new OutputItem();

        outputItem.setContractId(reportTranformation.transformField("contractId", input));
        outputItem.setFirstName(reportTranformation.transformField("firstName", input));
        outputItem.setLastName(reportTranformation.transformField("lastName", input));
        outputItem.setJurisdiction(reportTranformation.transformField("jurisdiction", input));

        return outputItem;
    }

}
