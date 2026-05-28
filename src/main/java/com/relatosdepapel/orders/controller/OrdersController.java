package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.controller.model.CreateOrderRequestDto;
import com.relatosdepapel.orders.controller.model.CreateOrderResponseDto;
import com.relatosdepapel.orders.controller.model.GetOrdersResponseDto;
import com.relatosdepapel.orders.controller.model.GetOrdersOwnerResponseDto;
import com.relatosdepapel.orders.controller.model.UpdateOrderItemStatusDto;
import com.relatosdepapel.orders.service.CreateOrdersService;
import com.relatosdepapel.orders.service.GetOrdersService;
import com.relatosdepapel.orders.service.UpdateOrderItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class OrdersController {

    private final CreateOrdersService createOrdersService;
    private final GetOrdersService getOrdersService;


    @GetMapping("orders")
    public ResponseEntity<GetOrdersResponseDto> getOrders(

            @RequestParam(required = false)
            Integer ownerId,

            @RequestParam(required = false)
            LocalDateTime orderDate,

            @RequestParam(required = false)
            BigDecimal minTotal
    ) {

        return ResponseEntity.ok(
                getOrdersService.getOrders(
                        ownerId,
                        orderDate,
                        minTotal
                )
        );
    }


    @GetMapping("orders/user/{ownerId}")
    public ResponseEntity<GetOrdersOwnerResponseDto> getOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(getOrdersService.getOrderByOwnerId(ownerId.intValue()));
    }
    @PostMapping("orders")
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(request));
    }

}
