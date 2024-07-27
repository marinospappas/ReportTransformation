package com.mpdev.reporting.validation;


import jakarta.validation.groups.Default;

public class ValidationGroups {
    public interface SecretRecord extends Default {}
    public interface ConfidentialRecord extends Default {}
    public interface PublicRecord extends Default {}

    public interface SecretRecordUS extends SecretRecord {}
    public interface ConfidentialRecordUS extends ConfidentialRecord {}
    public interface PublicRecordUS extends PublicRecord {}

    public interface SecretRecordNonUS extends SecretRecord {}
    public interface ConfidentialRecordNonUS extends ConfidentialRecord {}
    public interface PublicRecordNonUS extends PublicRecord {}
}
