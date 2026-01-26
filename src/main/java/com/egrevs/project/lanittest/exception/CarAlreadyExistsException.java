package com.egrevs.project.lanittest.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String message) {
        super(message);
    }
}
