package com.egrevs.project.lanittest.exception;

public class PersonIsNotAdultException extends RuntimeException {
    public PersonIsNotAdultException(String message) {
        super(message);
    }
}
