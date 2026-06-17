package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.controller.model.*;
import com.relatosdepapel.orders.service.CreateOrdersService;
import com.relatosdepapel.orders.service.GetOrdersService;
import com.relatosdepapel.orders.service.DeleteOrdersService;
import com.relatosdepapel.orders.service.PatchOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final CreateOrdersService createOrdersService;
    private final GetOrdersService getOrdersService;
    private final DeleteOrdersService deleteOrdersService;
    private final PatchOrderItem patchOrderItem;


    @GetMapping
    public ResponseEntity<GetOrdersResponseDto> getOrders(

            @RequestParam(required = false)
            Integer Id,

            @RequestParam(required = false)
            Integer ownerId,

            @RequestParam(required = false)
            LocalDateTime orderDate,

            @RequestParam(required = false)
            BigDecimal minTotal,
            @RequestParam(required = false,defaultValue = "5") Integer pageSize,
            @RequestParam(required = false,defaultValue = "0") Integer page

    ) {

        return ResponseEntity.ok(
                getOrdersService.getOrders(
                        ownerId,
                        orderDate,
                        minTotal,
                        pageSize,
                        page
                )
        );
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(request));
    }
    @GetMapping("user/{ownerId}")
    public ResponseEntity<GetOrdersOwnerResponseDto> getOwnerId(@PathVariable Long ownerId,
                                                                @RequestParam(required = false,defaultValue = "5") Integer pageSize,
                                                                @RequestParam(required = false,defaultValue = "0") Integer page) {
        return ResponseEntity.ok(getOrdersService.getOrderByOwnerId(ownerId.intValue(),page,pageSize));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        deleteOrdersService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("{id}")
    public ResponseEntity<Boolean> patchOrder(@PathVariable Integer id, @RequestBody UpdateOrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patchOrderItem.updateOrderItemStatus(id, orderDto.getStatus()));
    }

}
