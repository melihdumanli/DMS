package com.melihdumanli.dms.exception;

public enum ExceptionSeverity {

    INFO("DMS INFO"),
    WARNING("DMS WARNING"),
    ERROR("DMS ERROR"),
    FATAL("DMS FATAL");

    private final String text;

    ExceptionSeverity(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
