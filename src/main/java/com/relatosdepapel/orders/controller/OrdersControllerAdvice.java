package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.controller.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class OrdersControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .details(Optional.ofNullable(ex.getMessage()).orElse("Ocurrió un error interno al procesar la solicitud."))
                        .build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        log.error("Unexpected state while updating an order item", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .details(Optional.ofNullable(ex.getMessage()).orElse("Ocurrió un error interno al procesar la solicitud."))
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("Unhandled error while processing request", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .details(Optional.ofNullable(ex.getMessage()).orElse("Ocurrió un error interno al procesar la solicitud."))
                        .build());
    }
}
