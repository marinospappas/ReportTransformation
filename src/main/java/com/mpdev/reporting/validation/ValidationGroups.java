package com.mpdev.reporting.validation;

public class ValidationGroups {
    public interface SecretRecord {}
    public interface ConfidentialRecord {}
    public interface PublicRecord {}

    public interface SecretRecordUS extends SecretRecord {}
    public interface ConfidentialRecordUS extends ConfidentialRecord {}
    public interface PublicRecordUS extends PublicRecord {}

    public interface SecretRecordNonUS extends SecretRecord {}
    public interface ConfidentialRecordNonUS extends ConfidentialRecord {}
    public interface PublicRecordNonUS extends PublicRecord {}
}
