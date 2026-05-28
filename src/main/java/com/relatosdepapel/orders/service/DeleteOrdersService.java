package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.exception.OrderNotFoundException;
import com.relatosdepapel.orders.repository.OrderJpaRepository;
import com.relatosdepapel.orders.repository.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteOrdersService {

    private final OrderJpaRepository orderRepository;

    @Transactional
    public void deleteOrderByOwnerId(Long ownerId) {

        List<Order> orders = orderRepository
                .findByOwnerIdOrderByOrderDateDesc(ownerId.intValue());

        if (orders.isEmpty()) {
            throw new OrderNotFoundException(
                    "Order not found with ownerId: " + ownerId
            );
        }

        // Obtener la orden más reciente
        Order order = orders.get(0);

        // Eliminar la orden
        orderRepository.delete(order);
    }
}