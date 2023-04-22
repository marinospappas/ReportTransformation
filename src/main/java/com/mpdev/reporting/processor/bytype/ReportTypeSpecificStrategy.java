package com.mpdev.reporting.processor.bytype;

import com.mpdev.reporting.report.ItemType;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;

public interface ReportTypeSpecificStrategy {

    ItemType getReportType();

    OutputItem apply(final InputItem input);

}
