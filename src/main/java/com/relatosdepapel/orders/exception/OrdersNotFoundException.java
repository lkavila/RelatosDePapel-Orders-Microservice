package com.relatosdepapel.orders.exception;

public class OrdersNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrdersNotFoundException(String message) {
        super(message);
    }
    public OrdersNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
