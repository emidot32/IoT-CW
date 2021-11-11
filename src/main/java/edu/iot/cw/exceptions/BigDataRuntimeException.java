package edu.iot.cw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Something went wrong")
public class BigDataRuntimeException extends RuntimeException {

    public BigDataRuntimeException(String message) {
        super(message);
    }
}
