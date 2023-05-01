package com.mpdev.reporting.report.inreport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InputItem {

    @JsonProperty("Contract Id")
    private String contractId;
    @JsonProperty("Last Name")
    private String lastName;
    @JsonProperty("First Name")
    private String firstName;
    @JsonProperty("Item Type")
    private String itemType;
    @JsonProperty("Contract Type")
    private String contractType;
    @JsonProperty("Start Date")
    private String startDate;
    @JsonProperty("End Date")
    private String endDate;
    @JsonProperty("Contract Value")
    private String contractValue;
    @JsonProperty("Jurisdiction")
    private String jurisdiction;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Agreement Number")
    private String agreementNumber;
    @JsonProperty("Comments")
    private String comments;
}