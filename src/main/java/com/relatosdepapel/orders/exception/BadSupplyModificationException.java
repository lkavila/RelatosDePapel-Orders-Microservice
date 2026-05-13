package com.relatosdepapel.orders.exception;

public class BadSupplyModificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadSupplyModificationException(String message) {
        super(message);
    }
    public BadSupplyModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
