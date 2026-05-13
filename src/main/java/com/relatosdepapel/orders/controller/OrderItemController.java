package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.controller.model.UpdateOrderItemStatusDto;
import com.relatosdepapel.orders.service.UpdateOrderItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderItemController {

    private final UpdateOrderItemStatus updateOrderItemStatus;

    @PatchMapping("order-items")
    public ResponseEntity<UpdateOrderItemStatusDto>  updateOrderItemStatus(@RequestBody UpdateOrderItemStatusDto updateOrderItemStatusDto) {
        updateOrderItemStatus.updateOrderItemStatus(updateOrderItemStatusDto.id, updateOrderItemStatusDto.status);
        return ResponseEntity.ok(updateOrderItemStatusDto);
    }
}
