package com.melihdumanli.dms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DmsBusinessException extends RuntimeException {

    public DmsBusinessException(ExceptionSeverity severity, String message) {
        super("[" + severity.getText() + "] " + message);
    }
}
