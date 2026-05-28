package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.controller.model.UpdateOrderItemDto;
import com.relatosdepapel.orders.service.PatchOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderItemController {

    private final PatchOrderItem patchOrderItem;

    @PatchMapping("order-items/{id}")
    public ResponseEntity<UpdateOrderItemDto>  patchOrderItem(@PathVariable Integer id, @RequestBody UpdateOrderItemDto updateOrderItemStatusDto) {
        UpdateOrderItemDto result = patchOrderItem.patchOrderItem(id, updateOrderItemStatusDto);
        return ResponseEntity.ok(result);
    }
}
