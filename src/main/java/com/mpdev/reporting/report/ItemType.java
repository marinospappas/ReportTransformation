package com.mpdev.reporting.report;

public enum ItemType {

    Public("P"),
    Confidential("C"),
    Secret("S"),
    None("N");

    private final String abbreviation;

    ItemType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
