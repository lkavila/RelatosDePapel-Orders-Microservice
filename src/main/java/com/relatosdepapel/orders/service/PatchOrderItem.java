package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.controller.model.UpdateOrderItemDto;
import com.relatosdepapel.orders.repository.OrderItemJpaRepository;
import com.relatosdepapel.orders.repository.model.OrderItem;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PatchOrderItem {
    
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Transactional
    public UpdateOrderItemDto patchOrderItem(
            Integer id,
            UpdateOrderItemDto dto
    ) {
        OrderItem item = orderItemJpaRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Order item not found"
                        ));

        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }

        if (dto.getQuantity() != null) {
            item.setQuantity(dto.getQuantity());
        }

        if (dto.getSubTotal() != null) {
            item.setSubTotal(dto.getSubTotal());
        }

        orderItemJpaRepository.save(item);

        return UpdateOrderItemDto.builder()
                .id(id)
                .status(dto.getStatus())
                .quantity(dto.getQuantity())
                .subTotal(dto.getSubTotal())
                .build();
    }
}
