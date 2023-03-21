package com.mpdev.reporting.report.outreport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputItem {

    private String lastName;
    private String firstName;
    private String itemType;

}