package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.repository.OrderItemJpaRepository;
import com.relatosdepapel.orders.repository.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateOrderItemStatus {
    
    private final OrderItemJpaRepository orderItemJpaRepository;
    
    public void updateOrderItemStatus(Integer orderItemId, OrderStatus orderStatus) {
        System.out.println("Updating order item with ID: " + orderItemId + " to status: " + orderStatus);
        var orderItem = orderItemJpaRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Orden item not found with ID: " + orderItemId));


        orderItem.setStatus(orderStatus);
        try {
            orderItemJpaRepository.save(orderItem);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order item status: " + e.getMessage(), e);
        }
    }
}
