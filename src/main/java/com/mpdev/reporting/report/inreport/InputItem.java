package com.mpdev.reporting.report.inreport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputItem {

    @JsonProperty("Last Name")
    private String lastName;
    @JsonProperty("First Name")
    private String firstName;
    @JsonProperty("Item Type")
    private String itemType;
}