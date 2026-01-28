package com.egrevs.project.lanittest.exception;

public class PersonWithoutCarsException extends RuntimeException {
    public PersonWithoutCarsException(String message) {
        super(message);
    }
}
