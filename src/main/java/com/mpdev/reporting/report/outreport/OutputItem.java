package com.mpdev.reporting.report.outreport;

import com.mpdev.reporting.validation.ValidationGroups.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputItem {

    @NotNull(message = "EX100: Contract Id must be 8 characters long")
    @Size(min = 8, max = 8, message = "EX100: Contract Id must be 8 characters long")
    private String contractId;
    @NotEmpty(message = "EX101: Last Name cannot be empty", groups = PublicRecord.class)
    @Size(min = 1, max = 1, message = "EX102: Last Name must only be initial for Confidential or Secret record",
            groups = {ConfidentialRecord.class, SecretRecord.class})
    @NotNull(message = "EX102: Last Name must only be initial for Confidential or Secret record",
            groups = {ConfidentialRecord.class, SecretRecord.class})
    private String lastName;
    private String firstName;
    private String itemType;
    private String jurisdiction;
    private String endDate;
    @Size(max = 0, message = "EX103: Agreement Number must be empty for Secret record", groups = SecretRecord.class)
    @NotEmpty(message = "EX104: Agreement Number must be filled in for non Secret record", groups = {ConfidentialRecord.class, PublicRecord.class})
    private String agreementNumber;
}