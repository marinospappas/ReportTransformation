package com.mpdev.reporting.report.outreport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputItem {

    @Size(min = 8, max = 8, message = "EX100: Contract Id must be 8 characters long")
    private String contractId;
    @NotEmpty(message = "EX101: Last Name cannot be empty", groups = {} )
    private String lastName;
    private String firstName;
    private String itemType;

}